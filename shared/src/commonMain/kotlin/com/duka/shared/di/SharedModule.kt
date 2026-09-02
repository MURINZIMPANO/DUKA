package com.duka.shared.di

import com.duka.shared.data.SeedDataManager
import com.duka.shared.data.session.SessionManager
import com.duka.shared.domain.EbmGateway
import com.duka.shared.domain.MockEbmGateway
import com.duka.shared.domain.RegexVoiceEntryParser
import com.duka.shared.domain.VoiceEntryParser
import com.duka.shared.notifications.NotificationPreferences
import org.koin.dsl.module

/**
 * Shared Koin module — provides business logic and common dependencies.
 * Platform-specific modules (drivers, DAOs) are provided separately.
 */
val sharedModule = module {
    // Domain
    single<EbmGateway> { MockEbmGateway() }
    single<VoiceEntryParser> { RegexVoiceEntryParser() }

    // Notifications
    single { NotificationPreferences(get()) }

    // Seed data manager — depends on shared repositories and settings
    single {
        SeedDataManager(
            businessRepository = get(),
            userRepository = get(),
            productRepository = get(),
            saleRepository = get(),
            employeeRepository = get(),
            feedbackReportRepository = get(),
            budgetGoalRepository = get(),
            shopRatingRepository = get(),
            chatRepository = get(),
            settings = get()
        )
    }
}

/**
 * Session Koin module — needs SessionManager which needs platform driver.
 * Provided after platform-specific settings driver is initialized.
 */
fun sessionModule(sessionManager: SessionManager) = module {
    single { sessionManager }
}
