package com.duka.shared.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.duka.shared.db.DukaDatabase
import com.duka.shared.data.session.SessionManager
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val iosPlatformModule = module {
    single<NativeSqliteDriver> {
        NativeSqliteDriver(
            schema = DukaDatabase.Schema,
            name = "duka.db"
        )
    }

    single<ObservableSettings> {
        NSUserDefaultsSettings("duka_session")
    }

    single {
        SessionManager(
            settings = get(),
            userRepository = get()
        )
    }
}
