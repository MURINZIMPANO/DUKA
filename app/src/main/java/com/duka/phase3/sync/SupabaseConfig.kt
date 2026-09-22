package com.duka.phase3.sync

/**
 * Phase 3 — Supabase configuration.
 *
 * Paste your project's URL and anon (public) key here, or better, provide them via
 * `local.properties` / environment at build time. The anon key is safe to embed in a
 * client app ONLY because row-level security (see supabase/migration.sql) restricts
 * what anonymous/authenticated clients can read and write. Never put the service_role
 * key in the app — that key belongs only to the admin web portal's server side.
 */
object SupabaseConfig {
    // TODO: replace with your project values (Supabase Dashboard → Settings → API)
    const val URL: String = "https://YOUR-PROJECT-ref.supabase.co"
    const val ANON_KEY: String = "YOUR-SUPABASE-ANON-KEY"

    const val IS_CONFIGURED: Boolean =
        URL.startsWith("https://") && !URL.contains("YOUR-PROJECT") &&
                !ANON_KEY.contains("YOUR-SUPABASE")

    val restUrl: String get() = "$URL/rest/v1"
    val authUrl: String get() = "$URL/auth/v1"
    val storagePublicUrl: String get() = "$URL/storage/v1/object/public"
}
