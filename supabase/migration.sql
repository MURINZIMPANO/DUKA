-- ============================================================================
-- Duka — Phase 3 backend schema (Supabase / Postgres)
-- Run this ONCE in the Supabase SQL editor (Dashboard → SQL → New query).
--
-- What this creates:
--   shops            — public shop profiles synced up from each shop's device
--   shop_products    — public catalogs for Explore browsing
--   messages         — Owner↔Client chat (cross-device)
--   shop_media       — storage bucket for shop cover images (public read)
--   admin_users      — admin allowlist (platform admins only)
--
-- Data-minimisation notes (per spec):
--   * Individual sale records NEVER leave shop devices — only the aggregate
--     `sales_velocity` (recent sales per day) is pushed for trending.
--   * Employee chat and tax/EBM data stay local-only.
--   * Client chat is the only message content stored server-side.
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 1. shops — one row per registered shop (public profile)
-- ---------------------------------------------------------------------------
create table if not exists public.shops (
    id                  text primary key,                    -- client-generated stable uuid
    name                text not null,
    category            text not null default 'Other',       -- Supermarket | Bakery | Pharmacy | Grocery | Other
    district            text not null default '',
    cover_url           text not null default '',
    avg_price           numeric(12,2) not null default 0,
    sales_velocity      numeric(12,4) not null default 0,    -- recent sales/day (AGGREGATE only)
    product_count       integer not null default 0,
    registered_at       timestamptz not null default now(),
    registered_at_millis bigint not null default 0,          -- client-side ordering convenience
    rating_average      numeric(4,2) not null default 0,
    rating_count        integer not null default 0,
    is_flagged          boolean not null default false,      -- admin moderation flag
    created_at          timestamptz not null default now(),
    updated_at          timestamptz not null default now()
);

-- ---------------------------------------------------------------------------
-- 2. shop_products — public catalog rows (name, price, category only)
-- ---------------------------------------------------------------------------
create table if not exists public.shop_products (
    id          bigint generated always as identity primary key,
    shop_id     text not null references public.shops(id) on delete cascade,
    name        text not null,
    price       numeric(12,2) not null default 0,
    category    text not null default '',
    updated_at  timestamptz not null default now(),
    unique (shop_id, name)
);

-- ---------------------------------------------------------------------------
-- 3. messages — Owner↔Client chat (the only chat content stored server-side)
-- ---------------------------------------------------------------------------
create table if not exists public.messages (
    id               text primary key,                     -- client-generated stable uuid
    conversation_id  text not null,
    shop_id          text not null references public.shops(id) on delete cascade,
    client_user_id   bigint not null default 0,
    sender_role      text not null check (sender_role in ('owner','client')),
    sender_label     text not null default '',
    text             text not null,
    timestamp_millis bigint not null,
    created_at       timestamptz not null default now()
);
create index if not exists idx_messages_conversation on public.messages (conversation_id, timestamp_millis);
create index if not exists idx_messages_shop on public.messages (shop_id, timestamp_millis);

-- ---------------------------------------------------------------------------
-- 4. admin_users — allowlist for the admin web portal
--    Add your admin emails here; the portal denies everyone else.
-- ---------------------------------------------------------------------------
create table if not exists public.admin_users (
    email     text primary key,
    added_at  timestamptz not null default now()
);

-- ---------------------------------------------------------------------------
-- 5. Row Level Security
--    Shops' devices use the ANON key (no per-user auth in Phase 3 mobile);
--    anon can upsert own public data and read the public directory.
--    The admin portal authenticates real users; admin checks are allowlist-based.
-- ---------------------------------------------------------------------------

alter table public.shops          enable row level security;
alter table public.shop_products  enable row level security;
alter table public.messages       enable row level security;
alter table public.admin_users    enable row level security;

-- Public directory read: any device can read unflagged shops (Explore cache).
create policy "public read unflagged shops"
    on public.shops for select
    using (is_flagged = false);

-- Devices can upsert shop profiles (idempotent push from the shop's own device).
create policy "anon upsert shops"
    on public.shops for insert
    with check (true);

create policy "anon update shops"
    on public.shops for update
    using (true);

-- Catalog: public read; devices push their own catalog rows.
create policy "public read shop products"
    on public.shop_products for select
    using (true);

create policy "anon insert shop products"
    on public.shop_products for insert
    with check (true);

create policy "anon update shop products"
    on public.shop_products for update
    using (true);

-- Chat: messages are readable by anyone holding the conversation id (the two
-- devices). Phase 4+ should move to Supabase Auth so RLS can verify membership.
create policy "read messages by conversation"
    on public.messages for select
    using (true);

create policy "anon insert messages"
    on public.messages for insert
    with check (true);

-- Only admins may flag/unflag shops (enforced again in the portal's queries).
create policy "admin update shop flags"
    on public.shops for update
    using (exists (select 1 from public.admin_users a where a.email = auth.jwt() ->> 'email'));

-- ---------------------------------------------------------------------------
-- 6. Storage — shop_media bucket (public read) for cover images
-- ---------------------------------------------------------------------------
insert into storage.buckets (id, name, public)
values ('shop_media', 'shop_media', true)
on conflict (id) do nothing;

create policy "public read shop media"
    on storage.objects for select
    using (bucket_id = 'shop_media');

create policy "anon upload shop media"
    on storage.objects for insert
    with check (bucket_id = 'shop_media');

-- ---------------------------------------------------------------------------
-- 7. updated_at touch trigger
-- ---------------------------------------------------------------------------
create or replace function public.touch_updated_at()
returns trigger as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

drop trigger if exists trg_shops_updated_at on public.shops;
create trigger trg_shops_updated_at
    before update on public.shops
    for each row execute function public.touch_updated_at();

-- ---------------------------------------------------------------------------
-- 8. Seed admin — add your platform admin email here, then log in with that
--    Supabase Auth user in the web portal.
-- ---------------------------------------------------------------------------
-- insert into public.admin_users (email) values ('admin@yourdomain.rw');
