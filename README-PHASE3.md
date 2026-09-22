# Duka — Phase 3: Explore, Category Templates, Chat, Admin Portal

Phase 3 is **strictly additive**. No existing V1–V6 screen, table, repository, or
navigation behaviour was modified except for three surgical, backwards-compatible
hook points listed in "What changed in existing files" below.

---

## 1. Architecture change — real backend, offline-first preserved

Phase 3 introduces **Supabase** (managed Postgres + REST + auth + storage).

- **Local-first for writes.** Each shop's Room database remains the source of
  truth for daily operation. Sales, employee chat, tax/EBM data **never leave
  the device**.
- **Shared for discovery.** Only public-facing data syncs:
  - shop profile (name, category, district, cover image, rating, aggregate stats)
  - product catalog (name, price, category)
  - one aggregate number for "trending": recent sales per day (`sales_velocity`)
  - Owner↔Client chat messages (inherently cross-device)
- **Sync is visible & retriable.** `ExploreSyncService.syncState` exposes every
  failure. The Explore screen shows "Syncing…", "Up to date", or a tappable
  error banner — never a silent failure. Explore reads from the local cache, so
  it stays usable offline and shows last-synced data.

Supabase was chosen over a hand-rolled Node/Express server for managed Postgres,
auto REST API, auth, and storage out of the box. A custom backend remains the
right migration path if limits are hit.

## 2. Explore screen

Structure (top to bottom), per spec:
1. **Search** — shops and products, live filtering
2. **Category strip** — Supermarket / Bakery / Pharmacy / Grocery / Other chips; nothing selected by default
3. **Trending near you** — ranked by `sales_velocity` (district-first ordering heuristic, then national)
4. **New on Duka** — most recently registered shops
5. **Suggested for you** — **heuristic, not AI**: popular-in-district today; will use opened-shop history once tracked
6. **Browse all shops** — every registered shop, **no default filtering**; location/rating/price filters are opt-in and off by default

Design uses the existing token system (Canvas/Forest/Mist/Amber), editorial
hierarchy, generous whitespace, capped card density, restrained motion. No
autoplay video, no stories/reels, no algorithmic infinite scroll.

**Honesty note — imagery:** shop cards use clean category-icon placeholders.
No branded-product photography is claimed or faked; real product photography
would be a separate, deliberate licensing/sourcing step.

## 3. Category starter templates

`com.duka.phase3.data.CategoryTemplates` — a lookup keyed by category. When an
owner opens an **empty Products screen** and their category has a template, they
get one offer per session: *"Quick start — add N common [category] products?"*
Accepting inserts selectable starter items with **placeholder RWF pricing** the
owner can edit, remove, or add to. Declining inserts nothing.

**Honesty note — content:** the seeded lists for Supermarket, Bakery, Pharmacy,
and Grocery are **samples, not exhaustive**, and need validation with real
Rwandan shop owners before being presented as complete. Items are generic
(no brands); prices are editable defaults, not market data.

## 4. Chat — extended, not rebuilt

The V1/V2 Owner↔Employee chat pattern extends to Owner↔Client:

- Same message shape plus the spec's `participantType: "employee" | "client"` field
- New `client_chat_messages` table — the existing `chat_messages` table is untouched
- Cross-device via Supabase; the conversation screen **polls every 3s** while
  open (deliberate choice: polling keeps zero new dependencies and feels
  realtime at this scale — swap to supabase-kt realtime later without changing
  call sites)
- Reachable from **Shop Profile → Message** (client side) and **More → Client
  chats** (owner side), with a conversation list showing each client thread's
  latest message
- Offline sends are marked "not delivered yet" and retried automatically

## 5. Admin portal (`admin-portal/`)

Separate lightweight **Next.js** web app for platform administrators:

- Supabase Auth login, restricted by the `admin_users` allowlist
- Shop list: searchable, with status (Active/Flagged), category, district,
  registration date
- Shop detail: the shop's public profile exactly as Explore renders it, plus
  its full synced catalog
- Platform stats: totals, by category, by district, new this week/month
- One moderation action: **flag/unflag** — flagged shops disappear from Explore

Visually plain on purpose: tables and detail views; forest green only for
primary actions. See `admin-portal/README.md` for setup.

## 6. Backend setup (once)

1. Create a Supabase project (free tier is fine to start)
2. Run `supabase/migration.sql` in the SQL editor — creates `shops`,
   `shop_products`, `messages`, `admin_users`, RLS policies, and the
   `shop_media` storage bucket
3. Add your admin email to `admin_users` (see the comment at the end of the migration)
4. Paste your project URL + anon key into:
   - `app/src/main/java/com/duka/phase3/sync/SupabaseConfig.kt` (mobile)
   - `admin-portal/.env.local` (portal — copy from `.env.local.example`)
5. Until step 4 is done, the app runs **fully offline as before**; Explore shows
   "Backend not configured…" as a visible, honest state.

## 7. What changed in existing files (complete list)

| File | Change |
|---|---|
| `DukaDatabase.kt` | version 6→7, +3 new tables, +2 DAO accessors (additive migration) |
| `MainActivity.kt` | injects 2 new Phase 3 singletons; registers 4 new routes; adds "Explore" to client bottom bar; +1 More menu entry |
| `NavRoutes.kt` | +4 route constants (additive) |
| `ProductListScreen.kt` | one additive block: quick-start offer when product list is empty |
| `MoreScreen.kt` | one additive menu item with default no-op parameter |

Everything else is **new files only**. All pre-existing flows (owner, employee,
government admin, existing client tabs) behave exactly as before.

## 8. Definition of done — how to verify

1. **Two shops, two devices:** register shops in different categories/districts
   on two devices. Both appear in "New on Duka" and "Browse all shops" on a
   third device after its next sync (auto on opening Explore) — no manual sync trigger.
2. **Client messaging:** client opens a Shop Profile → Message → sends a message.
   The owner sees the conversation appear in More → Client chats without refreshing
   (3s poll).
3. **Admin portal:** open in an ordinary browser, sign in as an allowlisted admin:
   both test shops appear with accurate profile and catalog data.
4. **Category quick-start:** on a fresh shop in a templated category, the empty
   Products screen offers the starter list; accepting populates editable
   placeholder-priced products.

## 9. Known scope boundaries (deliberate)

- Cover images: storage bucket + URL column exist; the mobile UI still shows
  the category placeholder (upload flow is a later pass — no fake imagery).
- Realtime: websocket channels arrive with supabase-kt; polling is the Phase 3
  contract (`observeConversation`/`sendMessage` won't change).
- Client↔Owner RLS is conversation-id-based; Phase 4 should bind messages to
  Supabase Auth identities for stricter row security.
- The Explore "district" used for trending/suggested defaults to Kigali until
  the client profile surfaces a district field.
