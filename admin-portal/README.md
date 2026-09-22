# Duka Admin Portal — Phase 3

A small internal ops web app for platform administrators. Plain and functional
by design — tables and detail views, not a marketing site. Forest green is used
for primary actions only.

## Stack
- Next.js (App Router) + React
- `@supabase/supabase-js` talking to the same Supabase backend as the mobile app

## Setup

```bash
cd admin-portal
npm install
cp .env.local.example .env.local   # then paste your project's values
npm run dev                        # http://localhost:3100
```

## Admin access
Admins are allowlisted in the `admin_users` table (see `supabase/migration.sql`).
Add your email there, create that user in Supabase Auth, then sign in on the
portal. Anyone not on the allowlist is signed out with a clear message.

## What it shows (Phase 3 scope)
- **Shop list** — every registered shop, searchable, with status (active/flagged),
  category, district, and registration date
- **Shop detail** — the shop's public profile exactly as Explore renders it:
  profile info, product catalog, rating
- **Platform stats** — total shops, shops by category, by district, new
  registrations this week/month
- **Flag/unflag** — one moderation action; flagged shops disappear from Explore
