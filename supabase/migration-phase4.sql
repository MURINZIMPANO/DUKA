-- ============================================================================
-- Duka — Phase 4 backend addition (Supabase / Postgres)
-- Run this ONCE in the Supabase SQL editor, AFTER supabase/migration.sql.
--
-- What this creates:
--   client_sales — cross-device client purchases.
--
-- Data-minimisation notes (extends the Phase 3 policy on purpose):
--   * ONLY client-initiated purchases are stored server-side. Owner/employee/
--     voice sales remain local-only, exactly as before. This is the first
--     feature that puts an individual transaction on the server — it exists
--     so the OWNER device can see client purchases made remotely, which is
--     the whole point of Phase 4. Row payloads stay minimal (product name,
--     price, quantity, shop id) — no client identity beyond the numeric id.
--   * Employee chat and tax/EBM data stay local-only (unchanged).
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 1. client_sales — one row per confirmed client purchase (pushed by the
--    client's device; pulled by the owner's device to update income, top
--    products, stock, and the chat notice).
-- ---------------------------------------------------------------------------
create table if not exists public.client_sales (
    id                text primary key,                     -- client-generated deterministic uuid
    shop_id           text not null references public.shops(id) on delete cascade,
    shop_name         text not null default '',
    product_name      text not null,
    unit_price        numeric(12,2) not null default 0,
    quantity          integer not null default 1,
    total             numeric(12,2) not null default 0,
    receipt_number    text not null default '',
    client_user_id    bigint not null default 0,
    timestamp_millis  bigint not null,
    created_at        timestamptz not null default now()
);

create index if not exists idx_client_sales_shop on public.client_sales (shop_id, timestamp_millis);

-- ---------------------------------------------------------------------------
-- 2. Row Level Security — same anon posture as Phase 3 (no per-user auth yet).
--    Read is scoped to shop rows: anyone can read a shop's purchases with the
--    shop id, which is exactly what the owner device needs. Phase 4+ should
--    move to Supabase Auth so RLS can verify the requester owns the shop.
-- ---------------------------------------------------------------------------
alter table public.client_sales enable row level security;

create policy "public read client sales"
    on public.client_sales for select
    using (true);

create policy "anon insert client sales"
    on public.client_sales for insert
    with check (true);

-- ---------------------------------------------------------------------------
-- 3. Chat notices — Phase 4 sends a lightweight system-style message into the
--    existing Owner↔Client chat when a client purchase goes through. Phase 3's
--    CHECK constraint only allowed 'owner' | 'client', so widen it to 'system'.
--    Idempotent: re-running this migration is safe.
-- ---------------------------------------------------------------------------
alter table public.messages
    drop constraint if exists messages_sender_role_check;
alter table public.messages
    add constraint messages_sender_role_check
    check (sender_role in ('owner','client','system'));
