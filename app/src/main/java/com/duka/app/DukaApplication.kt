package com.duka.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.room.Room
import com.duka.app.data.local.DukaDatabase
import com.duka.app.data.session.SessionManager
import com.duka.android.data.di.androidRepositoryModule
import com.duka.android.data.di.androidViewModelModule
import com.duka.shared.di.androidPlatformModule
import com.duka.shared.di.sharedModule
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import javax.inject.Inject

@HiltAndroidApp
class DukaApplication : Application() {

    @Inject lateinit var initializer: DukaInitializer
    @Inject lateinit var sessionManager: SessionManager

    override fun onCreate() {
        super.onCreate()

        // Build the Room database (shared instance for both Hilt and Koin)
        val database = Room.databaseBuilder(
            applicationContext,
            DukaDatabase::class.java,
            "duka.db"
        )
            .addMigrations(
                DukaDatabase.MIGRATION_3_4,
                DukaDatabase.MIGRATION_4_5,
                DukaDatabase.MIGRATION_5_6,
                DukaDatabase.MIGRATION_6_7,
                DukaDatabase.MIGRATION_7_8
            )
            .build()

        // Initialize Koin for shared module + Android ViewModels
        startKoin {
            androidContext(this@DukaApplication)
            modules(
                sharedModule,          // shared Koin module (domain, notifications)
                androidPlatformModule, // shared Android platform module (settings, session)
                androidRepositoryModule(database), // shared repository implementations via Koin
                androidViewModelModule // shared ViewModels via Koin
            )
        }

        initializer.initialize()

        // Seed shared demo data on first launch (idempotent)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val seedManager: com.duka.shared.data.SeedDataManager = org.koin.java.KoinJavaComponent.get(com.duka.shared.data.SeedDataManager::class.java)
                seedManager.seedIfNeeded()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Apply persisted language before any UI renders
        CoroutineScope(Dispatchers.IO).launch {
            val langCode = sessionManager.languageCode.first()
            applyLocale(langCode)
        }
    }

    companion object {
        fun applyLocale(languageCode: String) {
            val localeList = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(localeList)
        }

        fun languageTagFromName(name: String): String = when (name) {
            "Kinyarwanda", "Ikinyarwanda" -> "rw"
            "French", "Français" -> "fr"
            else -> "en"
        }
    }
}
