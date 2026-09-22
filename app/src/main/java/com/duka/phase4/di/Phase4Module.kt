package com.duka.phase4.di

import com.duka.app.data.local.DukaDatabase
import com.duka.phase4.data.ClientPurchaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Phase 4 — Hilt bindings for the client purchase flow.
 *
 * SCOPE-GUARD: adds new providers only. Existing modules in com.duka.app.di
 * and the Phase 3 module are untouched.
 *
 * NOTES:
 *  * ClientChatMessageDao + SupabaseRestClient bindings come from Phase3Module —
 *    re-providing them here would be a duplicate-binding compile error.
 *  * ClientPurchaseSyncService is injected via its own @Inject constructor
 *    (same pattern as Phase3Repository), so only the DAO needs a provider here.
 */
@Module
@InstallIn(SingletonComponent::class)
object Phase4Module {

    @Provides
    @Singleton
    fun provideClientPurchaseDao(db: DukaDatabase): ClientPurchaseDao = db.clientPurchaseDao()
}
