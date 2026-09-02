package com.duka.shared.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.duka.shared.db.DukaDatabase
import com.duka.shared.data.session.SessionManager
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.ObservableSettings
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val androidPlatformModule = module {
    single<AndroidSqliteDriver> {
        AndroidSqliteDriver(
            schema = DukaDatabase.Schema,
            context = androidContext(),
            name = "duka.db"
        )
    }

    single<ObservableSettings> {
        val context = androidContext()
        val prefs = context.getSharedPreferences("duka_session", Context.MODE_PRIVATE)
        SharedPreferencesSettings(prefs)
    }

    single {
        SessionManager(
            settings = get(),
            userRepository = get()
        )
    }
}
