package com.duka.shared

import com.duka.shared.data.SeedDataManager
import com.duka.shared.di.iosPlatformModule
import com.duka.shared.di.sharedModule
import com.duka.shared.di.iosRepositoryModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

object KoinHelper : KoinComponent

private val seedScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

fun doInitKoin() {
    startKoin {
        modules(
            sharedModule,
            iosPlatformModule,
            iosRepositoryModule
        )
    }
}

/**
 * Non-suspend helper for Swift — launches seedIfNeeded in a coroutine.
 */
fun seedAppData() {
    val seedManager: SeedDataManager = KoinHelper.get()
    seedScope.launch {
        seedManager.seedIfNeeded()
    }
}
