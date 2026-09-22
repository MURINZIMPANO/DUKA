package com.duka.phase3.di

import com.duka.app.data.local.dao.ClientChatMessageDao
import com.duka.app.data.local.dao.ExploreShopDao
import com.duka.app.data.local.DukaDatabase
import com.duka.phase3.chat.ClientChatService
import com.duka.phase3.data.Phase3Repository
import com.duka.phase3.data.RemoteShopProductDao
import com.duka.phase3.sync.ExploreSyncService
import com.duka.phase3.sync.SupabaseRestClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Phase 3 — Hilt bindings.
 *
 * SCOPE-GUARD: adds new providers only. The existing DatabaseModule, SessionModule,
 * WorkManagerModule, and DomainModule in com.duka.app.di are untouched.
 */
@Module
@InstallIn(SingletonComponent::class)
object Phase3Module {

    @Provides
    @Singleton
    fun provideSupabaseRestClient(): SupabaseRestClient = SupabaseRestClient()

    @Provides
    @Singleton
    fun provideExploreShopDao(db: DukaDatabase): ExploreShopDao = db.exploreShopDao()

    @Provides
    @Singleton
    fun provideClientChatMessageDao(db: DukaDatabase): ClientChatMessageDao = db.clientChatMessageDao()

    @Provides
    @Singleton
    fun provideRemoteShopProductDao(db: DukaDatabase): RemoteShopProductDao = db.remoteShopProductDao()

    @Provides
    @Singleton
    fun provideExploreSyncService(
        businessRepository: com.duka.app.data.repository.BusinessRepository,
        productRepository: com.duka.app.data.repository.ProductRepository,
        saleRepository: com.duka.app.data.repository.SaleRepository,
        exploreShopDao: ExploreShopDao,
        client: SupabaseRestClient
    ): ExploreSyncService = ExploreSyncService(
        businessRepository = businessRepository,
        productRepository = productRepository,
        saleRepository = saleRepository,
        exploreShopDao = exploreShopDao,
        client = client
    )

    @Provides
    @Singleton
    fun provideClientChatService(
        dao: ClientChatMessageDao,
        client: SupabaseRestClient
    ): ClientChatService = ClientChatService(dao, client)

    @Provides
    @Singleton
    fun providePhase3Repository(
        exploreShopDao: ExploreShopDao,
        remoteProductDao: RemoteShopProductDao,
        clientChatMessageDao: ClientChatMessageDao,
        syncService: ExploreSyncService,
        client: SupabaseRestClient
    ): Phase3Repository = Phase3Repository(
        exploreShopDao,
        remoteProductDao,
        clientChatMessageDao,
        syncService,
        client
    )
}
