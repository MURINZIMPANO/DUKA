package com.duka.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.duka.app.data.repository.*
import com.duka.app.domain.EbmGateway
import com.duka.app.domain.VoiceEntryParser
import com.duka.app.ui.navigation.BottomNavItem
import com.duka.app.ui.navigation.NavRoutes
import com.duka.app.ui.screens.*
import com.duka.employee.management.*
import com.duka.settings.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.ui.graphics.vector.ImageVector
import com.duka.app.ui.screens.v3.*
import com.duka.app.ui.screens.v4.*
import com.duka.app.ui.theme.DukaTheme
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import com.duka.app.viewmodel.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var businessRepository: BusinessRepository
    @Inject lateinit var productRepository: ProductRepository
    @Inject lateinit var saleRepository: SaleRepository
    @Inject lateinit var employeeRepository: EmployeeRepository
    @Inject lateinit var chatRepository: ChatRepository
    @Inject lateinit var promoRepository: PromoRepository
    @Inject lateinit var issueRepository: IssueRepository
    @Inject lateinit var ebmRepository: EbmRepository
    @Inject lateinit var taxRepository: TaxRepository
    @Inject lateinit var ebmGateway: EbmGateway
    @Inject lateinit var voiceEntryParser: VoiceEntryParser
    // V3/V4 repositories
    @Inject lateinit var purchaseRepository: PurchaseRepository
    @Inject lateinit var budgetGoalRepository: BudgetGoalRepository
    @Inject lateinit var feedbackReportRepository: FeedbackReportRepository
    @Inject lateinit var shopRatingRepository: ShopRatingRepository
    @Inject lateinit var wholesalerRepository: WholesalerRepository
    @Inject lateinit var restockRequestRepository: RestockRequestRepository
    @Inject lateinit var sessionManager: com.duka.app.data.session.SessionManager
    // V5/V6 repositories
    @Inject lateinit var productAlertRepository: com.duka.app.data.repository.ProductAlertRepository
    @Inject lateinit var appNotificationRepository: com.duka.app.data.repository.AppNotificationRepository
    @Inject lateinit var stockAdjustmentRepository: com.duka.app.data.repository.StockAdjustmentRepository
    @Inject lateinit var expenseRepository: com.duka.app.data.repository.ExpenseRepository
    // Phase 3 (Explore + client chat)
    @Inject lateinit var phase3Repository: com.duka.phase3.data.Phase3Repository
    @Inject lateinit var clientChatService: com.duka.phase3.chat.ClientChatService
    // Phase 4 (client purchase flow + instant EBM receipt)
    @Inject lateinit var clientPurchaseSyncService: com.duka.phase4.sync.ClientPurchaseSyncService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DukaTheme {
                DukaApp(
                    businessRepository = businessRepository,
                    productRepository = productRepository,
                    saleRepository = saleRepository,
                    chatRepository = chatRepository,
                    promoRepository = promoRepository,
                    issueRepository = issueRepository,
                    ebmRepository = ebmRepository,
                    taxRepository = taxRepository,
                    ebmGateway = ebmGateway,
                    voiceEntryParser = voiceEntryParser,
                    employeeRepository = employeeRepository,
                    sessionManager = sessionManager,
                    purchaseRepository = purchaseRepository,
                    budgetGoalRepository = budgetGoalRepository,
                    feedbackReportRepository = feedbackReportRepository,
                    shopRatingRepository = shopRatingRepository,
                    wholesalerRepository = wholesalerRepository,
                    restockRequestRepository = restockRequestRepository,
                    productAlertRepository = productAlertRepository,
                    appNotificationRepository = appNotificationRepository,
                    stockAdjustmentRepository = stockAdjustmentRepository,
                    expenseRepository = expenseRepository,
                    phase3Repository = phase3Repository,
                    clientChatService = clientChatService,
                    clientPurchaseSyncService = clientPurchaseSyncService
                )
            }
        }
    }
}

@Composable
fun DukaApp(
    businessRepository: BusinessRepository,
    productRepository: ProductRepository,
    saleRepository: SaleRepository,
    chatRepository: ChatRepository,
    promoRepository: PromoRepository,
    issueRepository: IssueRepository,
    ebmRepository: EbmRepository,
    taxRepository: TaxRepository,
    ebmGateway: EbmGateway,
    voiceEntryParser: VoiceEntryParser,
    employeeRepository: EmployeeRepository,
    sessionManager: com.duka.app.data.session.SessionManager,
    // V3/V4 repositories
    purchaseRepository: PurchaseRepository,
    budgetGoalRepository: BudgetGoalRepository,
    feedbackReportRepository: FeedbackReportRepository,
    shopRatingRepository: ShopRatingRepository,
    wholesalerRepository: WholesalerRepository,
    restockRequestRepository: RestockRequestRepository,
    // V5/V6 repositories
    productAlertRepository: com.duka.app.data.repository.ProductAlertRepository,
    appNotificationRepository: com.duka.app.data.repository.AppNotificationRepository,
    stockAdjustmentRepository: com.duka.app.data.repository.StockAdjustmentRepository,
    expenseRepository: com.duka.app.data.repository.ExpenseRepository,
    // Phase 3
    phase3Repository: com.duka.phase3.data.Phase3Repository,
    clientChatService: com.duka.phase3.chat.ClientChatService,
    // Phase 4
    clientPurchaseSyncService: com.duka.phase4.sync.ClientPurchaseSyncService
) {
    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()

    // Determine start destination from session state
    val startDestination = when {
        sessionState.isLoading -> NavRoutes.SPLASH
        else -> when (sessionState.destination) {
            is SessionViewModel.NavDestination.Login -> NavRoutes.LOGIN
            is SessionViewModel.NavDestination.CompanySignup -> NavRoutes.COMPANY_SIGNUP
            is SessionViewModel.NavDestination.ClientSignup -> NavRoutes.CLIENT_SIGNUP
            is SessionViewModel.NavDestination.EmployeeLogin -> NavRoutes.EMPLOYEE_LOGIN
            is SessionViewModel.NavDestination.Dashboard -> NavRoutes.DASHBOARD
            is SessionViewModel.NavDestination.EmployeeSell -> NavRoutes.EMPLOYEE_SELL
            is SessionViewModel.NavDestination.ClientDiscover -> NavRoutes.CLIENT_DISCOVER
            is SessionViewModel.NavDestination.GovernmentAdmin -> NavRoutes.GOV_TAX_ENGINE
            is SessionViewModel.NavDestination.Splash -> NavRoutes.SPLASH
        }
    }

    // Navigate when destination changes (single source of truth)
    LaunchedEffect(sessionState.destination) {
        val target = when (sessionState.destination) {
            is SessionViewModel.NavDestination.Login -> NavRoutes.LOGIN
            is SessionViewModel.NavDestination.CompanySignup -> NavRoutes.COMPANY_SIGNUP
            is SessionViewModel.NavDestination.ClientSignup -> NavRoutes.CLIENT_SIGNUP
            is SessionViewModel.NavDestination.EmployeeLogin -> NavRoutes.EMPLOYEE_LOGIN
            is SessionViewModel.NavDestination.Dashboard -> NavRoutes.DASHBOARD
            is SessionViewModel.NavDestination.ClientDiscover -> NavRoutes.CLIENT_DISCOVER
            is SessionViewModel.NavDestination.GovernmentAdmin -> NavRoutes.GOV_TAX_ENGINE
            is SessionViewModel.NavDestination.EmployeeSell -> NavRoutes.EMPLOYEE_SELL
            is SessionViewModel.NavDestination.Splash -> NavRoutes.SPLASH
        }

        if (navController.currentDestination?.route != target) {
            navController.navigate(target) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // === Phase 4 — owner-side client purchase import (additive, no UI change) ===
    // Owners poll for client purchases made remotely; each becomes a local Sale
    // (source = "client") via the SAME repositories Employee Sell uses, plus a
    // stock decrement and a system-style chat notice. Client devices retry any
    // pending purchase pushes. Purely additive; no existing screen is touched.
    // (Role read locally — isOwner is declared further down and can't be referenced here.)
    val phase4IsOwner = sessionState.currentRole == "owner"
    if (phase4IsOwner) {
        androidx.compose.runtime.LaunchedEffect(sessionState.currentRole) {
            val businessId = sessionManager.getBusinessId()
            while (isActive) {
                try {
                    clientPurchaseSyncService.pullAndImportForOwner(businessId)
                } catch (_: Exception) { /* visible via syncState; never crash the app */ }
                delay(15_000)
            }
        }
    } else {
        // Client devices: complete any purchase pushes that were queued offline.
        androidx.compose.runtime.LaunchedEffect(sessionState.currentRole) {
            while (isActive) {
                try {
                    clientPurchaseSyncService.retryPending()
                } catch (_: Exception) { /* state stays visible in syncState */ }
                delay(30_000)
            }
        }
    }

    // Routes that show the bottom bar
    val bottomBarRoutes = setOf(
        NavRoutes.DASHBOARD, NavRoutes.PRODUCTS, NavRoutes.TAX_EBM,
        NavRoutes.CHAT, NavRoutes.MORE
    )
    val employeeBottomBarRoutes = setOf(
        NavRoutes.EMPLOYEE_SELL, NavRoutes.CHAT
    )
    // V3/V4: Client bottom bar routes
    val clientBottomBarRoutes = setOf(
        NavRoutes.CLIENT_DISCOVER, NavRoutes.EXPLORE, NavRoutes.CLIENT_BUDGET, NavRoutes.CLIENT_FEEDBACK
    )
    // V3/V4: Government admin bottom bar routes
    val govBottomBarRoutes = setOf(
        NavRoutes.GOV_TAX_ENGINE, NavRoutes.GOV_SECTOR_VIEW
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isOwner = sessionState.currentRole == "owner"
    val isClient = sessionState.currentRole == "client"
    val isGovAdmin = sessionState.currentRole == "government_admin"
    val showBottomBar = when {
        isOwner && currentRoute in bottomBarRoutes -> true
        !isOwner && !isClient && !isGovAdmin && currentRoute in employeeBottomBarRoutes -> true
        isClient && currentRoute in clientBottomBarRoutes -> true
        isGovAdmin && currentRoute in govBottomBarRoutes -> true
        else -> false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    when {
                        isOwner -> OwnerBottomBar(navController, currentRoute)
                        isClient -> ClientBottomBar(navController, currentRoute)
                        isGovAdmin -> GovAdminBottomBar(navController, currentRoute)
                        else -> EmployeeBottomBar(navController, currentRoute)
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeIn(tween(200))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(tween(200))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeIn(tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300, easing = FastOutSlowInEasing)
                    ) + fadeOut(tween(200))
                }
            ) {
                // Splash
                composable(NavRoutes.SPLASH) {
                    SplashScreen()
                }

                // Login
                composable(NavRoutes.LOGIN) {
                    LoginScreen(
                        isLoading = sessionState.isLoading,
                        error = sessionState.error,
                        onLogin = { phone, password ->
                            sessionViewModel.login(phone, password)
                        },
                        onGoToSignup = { sessionViewModel.goToSignup() },
                        onGoToClientSignup = { sessionViewModel.goToClientSignup() },
                        onGoToEmployeeLogin = { sessionViewModel.goToEmployeeLogin() }
                    )
                }

                // Company Signup (Owner)
                composable(NavRoutes.COMPANY_SIGNUP) {
                    RegisterScreen(
                        isLoading = sessionState.isLoading,
                        error = sessionState.error,
                        signupRole = "owner",
                        onCreateAccount = { ownerName, phone, password, bizName, bizType, empCount, lang, district ->
                            sessionViewModel.createBusinessAccount(
                                ownerName, phone, password, bizName, bizType, empCount, lang, district
                            )
                        },
                        onGoToLogin = { sessionViewModel.goToLogin() }
                    )
                }

                // Client Signup (Shopper/Client)
                composable(NavRoutes.CLIENT_SIGNUP) {
                    RegisterScreen(
                        isLoading = sessionState.isLoading,
                        error = sessionState.error,
                        signupRole = "client",
                        onCreateAccount = { _, _, _, _, _, _, _, _ -> },
                        onCreateClientAccount = { fullName, phone, password, lang ->
                            sessionViewModel.createClientAccount(
                                fullName, phone, password, lang
                            )
                        },
                        onGoToLogin = { sessionViewModel.goToLogin() }
                    )
                }

                // Employee Login (V5: code-only, no name needed)
                composable(NavRoutes.EMPLOYEE_LOGIN) {
                    EmployeeLoginScreen(
                        isLoading = sessionState.isLoading,
                        error = sessionState.error,
                        onEmployeeLogin = { code ->
                            sessionViewModel.employeeLogin(code)
                        },
                        onGoToLogin = { sessionViewModel.goToLogin() }
                    )
                }

                // Dashboard (Owner)
                composable(NavRoutes.DASHBOARD) {
                    DashboardScreen(
                        businessRepository = businessRepository,
                        productRepository = productRepository,
                        saleRepository = saleRepository,
                        employeeRepository = employeeRepository,
                        productAlertRepository = productAlertRepository,
                        onNavigateToAddProduct = { navController.navigate(NavRoutes.ADD_PRODUCT) },
                        onNavigateToTax = { navController.navigate(NavRoutes.TAX_EBM) },
                        onNavigateToVoiceAdd = { navController.navigate(NavRoutes.VOICE_ADD) },
                        onNavigateToTeam = { navController.navigate(NavRoutes.EMPLOYEE_MANAGEMENT) },
                        onNavigateToAlerts = { navController.navigate(NavRoutes.ALERT_CENTRE) },
                        onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) }
                    )
                }

                // Products
                composable(NavRoutes.PRODUCTS) {
                    ProductListScreen(
                        businessRepository = businessRepository,
                        productRepository = productRepository,
                        productAlertRepository = productAlertRepository,
                        onNavigateToAddProduct = { navController.navigate(NavRoutes.ADD_PRODUCT) },
                        onNavigateToEditProduct = { productId ->
                            navController.navigate(NavRoutes.editProductRoute(productId))
                        }
                    )
                }

                // Edit Product (V6)
                composable(
                    NavRoutes.EDIT_PRODUCT,
                    arguments = listOf(navArgument("productId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                    val bizId = kotlinx.coroutines.runBlocking { sessionManager.getBusinessId() }
                    com.duka.products.management.EditProductScreen(
                        productId = productId,
                        businessId = bizId,
                        productRepository = productRepository,
                        stockAdjustmentRepository = stockAdjustmentRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                // Alert Centre (V6)
                composable(NavRoutes.ALERT_CENTRE) {
                    val bizId = kotlinx.coroutines.runBlocking { sessionManager.getBusinessId() }
                    com.duka.notifications.AlertCentreScreen(
                        businessId = bizId,
                        productAlertRepository = productAlertRepository,
                        onBack = { navController.popBackStack() },
                        onAlertTap = { productId ->
                            navController.navigate(NavRoutes.editProductRoute(productId))
                        }
                    )
                }

                // Analytics Hub (V6)
                composable(NavRoutes.ANALYTICS_HUB) {
                    com.duka.analytics.AnalyticsHubScreen(
                        businessRepository = businessRepository,
                        saleRepository = saleRepository,
                        productRepository = productRepository,
                        expenseRepository = expenseRepository,
                        onBack = { navController.popBackStack() },
                        onNavigateToTaxEbm = { navController.navigate(NavRoutes.TAX_EBM) },
                        onNavigateToProducts = { navController.navigate(NavRoutes.PRODUCTS) }
                    )
                }

                // Owner Notifications (V6)
                composable(NavRoutes.OWNER_NOTIFICATIONS) {
                    val bizId = kotlinx.coroutines.runBlocking { sessionManager.getBusinessId() }
                    val uid = sessionState.currentUser?.id ?: 0L
                    com.duka.notifications.OwnerNotificationCentreScreen(
                        businessId = bizId,
                        userId = uid,
                        productAlertRepository = productAlertRepository,
                        appNotificationRepository = appNotificationRepository,
                        onBack = { navController.popBackStack() },
                        onAlertTap = { productId ->
                            navController.navigate(NavRoutes.editProductRoute(productId))
                        },
                        onNotificationTap = { route ->
                            if (route.isNotBlank()) navController.navigate(route)
                        }
                    )
                }

                // Client Notifications (V6)
                composable(NavRoutes.CLIENT_NOTIFICATIONS) {
                    val uid = sessionState.currentUser?.id ?: 0L
                    com.duka.notifications.ClientNotificationCentreScreen(
                        clientUserId = uid,
                        appNotificationRepository = appNotificationRepository,
                        onBack = { navController.popBackStack() },
                        onNotificationTap = { route ->
                            if (route.isNotBlank()) navController.navigate(route)
                        }
                    )
                }

                // Employee Notifications (V6)
                composable(NavRoutes.EMPLOYEE_NOTIFICATIONS) {
                    val uid = sessionState.currentUser?.id ?: 0L
                    val bizId = kotlinx.coroutines.runBlocking { sessionManager.getBusinessId() }
                    com.duka.notifications.EmployeeNotificationCentreScreen(
                        userId = uid,
                        businessId = bizId,
                        appNotificationRepository = appNotificationRepository,
                        onBack = { navController.popBackStack() },
                        onNotificationTap = { route ->
                            if (route.isNotBlank()) navController.navigate(route)
                        }
                    )
                }

                // Add Product
                composable(NavRoutes.ADD_PRODUCT) {
                    AddProductScreen(
                        businessRepository = businessRepository,
                        productRepository = productRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                // Employee Sell
                composable(NavRoutes.EMPLOYEE_SELL) {
                    EmployeeSellScreen(
                        businessRepository = businessRepository,
                        productRepository = productRepository,
                        saleRepository = saleRepository,
                        appNotificationRepository = appNotificationRepository,
                        employeeUserId = sessionState.currentUser?.id ?: 0L,
                        employeeBusinessId = kotlinx.coroutines.runBlocking { sessionManager.getBusinessId() },
                        employeeName = sessionState.currentUserName.ifBlank { "Employee" },
                        onLogout = { sessionViewModel.logout() },
                        onNavigateToSettings = { navController.navigate(NavRoutes.EMPLOYEE_SETTINGS) },
                        onNavigateToNotifications = { navController.navigate(NavRoutes.EMPLOYEE_NOTIFICATIONS) }
                    )
                }

                // Voice Add
                composable(NavRoutes.VOICE_ADD) {
                    VoiceAddScreen(
                        businessRepository = businessRepository,
                        productRepository = productRepository,
                        voiceEntryParser = voiceEntryParser,
                        onBack = { navController.popBackStack() }
                    )
                }

                // Tax & EBM
                composable(NavRoutes.TAX_EBM) {
                    TaxEbmScreen(
                        businessRepository = businessRepository,
                        saleRepository = saleRepository,
                        taxRepository = taxRepository,
                        ebmRepository = ebmRepository,
                        onNavigateToSendEbm = { saleId ->
                            navController.navigate(NavRoutes.sendEbmRoute(saleId))
                        }
                    )
                }

                // Send EBM
                composable(
                    NavRoutes.SEND_EBM,
                    arguments = listOf(navArgument("saleId") { type = NavType.LongType }),
                    enterTransition = {
                        scaleIn(tween(300, easing = FastOutSlowInEasing)) + fadeIn(tween(200))
                    },
                    exitTransition = {
                        fadeOut(tween(200))
                    }
                ) { backStackEntry ->
                    val saleId = backStackEntry.arguments?.getLong("saleId") ?: 0L
                    SendEbmScreen(
                        saleId = saleId,
                        saleRepository = saleRepository,
                        productRepository = productRepository,
                        ebmRepository = ebmRepository,
                        ebmGateway = ebmGateway,
                        onDismiss = { navController.popBackStack() }
                    )
                }

                // Chat
                composable(NavRoutes.CHAT) {
                    ChatScreen(
                        businessRepository = businessRepository,
                        chatRepository = chatRepository,
                        onBack = { navController.popBackStack() },
                        onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) }
                    )
                }

                // Promo
                composable(NavRoutes.PROMO) {
                    PromoScreen(
                        businessRepository = businessRepository,
                        promoRepository = promoRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                // Report Issue
                composable(NavRoutes.REPORT_ISSUE) {
                    ReportIssueScreen(
                        businessRepository = businessRepository,
                        issueRepository = issueRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                // More
                composable(NavRoutes.MORE) {
                    MoreScreen(
                        onNavigateToChat = { navController.navigate(NavRoutes.CHAT) },
                        onNavigateToPromo = { navController.navigate(NavRoutes.PROMO) },
                        onNavigateToReportIssue = { navController.navigate(NavRoutes.REPORT_ISSUE) },
                        onNavigateToCreditReadiness = { navController.navigate(NavRoutes.CREDIT_READINESS) },
                        onNavigateToTrendingShops = { navController.navigate(NavRoutes.TRENDING_SHOPS) },
                        onNavigateToRequestRestock = { navController.navigate(NavRoutes.REQUEST_RESTOCK) },
                        onNavigateToWholesalerMarketplace = { navController.navigate(NavRoutes.WHOLESALER_MARKETPLACE) },
                        onNavigateToRegionalRoadmap = { navController.navigate(NavRoutes.REGIONAL_ROADMAP) },
                        onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) },
                        onNavigateToEmployeeManagement = { navController.navigate(NavRoutes.EMPLOYEE_MANAGEMENT) },
                        onNavigateToAnalytics = { navController.navigate(NavRoutes.ANALYTICS_HUB) },
                        onNavigateToClientChats = { navController.navigate(NavRoutes.OWNER_CLIENT_CHAT_LIST) },
                        onLogout = { sessionViewModel.logout() }
                    )
                }

                // Unified Settings (all roles)
                composable(NavRoutes.SETTINGS) {
                    val settingsViewModel: com.duka.settings.shared.SettingsViewModel = hiltViewModel()
                    com.duka.settings.shared.SharedSettingsScreen(
                        role = sessionState.currentRole ?: "owner",
                        viewModel = settingsViewModel,
                        businessRepository = businessRepository,
                        employeeRepository = employeeRepository,
                        onBack = { navController.popBackStack() },
                        onNavigateToSuggestionsHistory = { navController.navigate(NavRoutes.SUGGESTIONS_HISTORY) },
                        onNavigateToDataPrivacy = { navController.navigate(NavRoutes.DATA_PRIVACY) }
                    )
                }

                // V5: Employee Management
                composable(NavRoutes.EMPLOYEE_MANAGEMENT) {
                    val empViewModel: com.duka.employee.management.EmployeeManagementViewModel = hiltViewModel()
                    com.duka.employee.management.EmployeeListScreen(
                        viewModel = empViewModel,
                        onBack = { navController.popBackStack() },
                        onAddEmployee = { navController.navigate(NavRoutes.ADD_EMPLOYEE) }
                    )
                }

                // V5: Add Employee
                composable(NavRoutes.ADD_EMPLOYEE) {
                    val empViewModel: com.duka.employee.management.EmployeeManagementViewModel = hiltViewModel()
                    com.duka.employee.management.AddEmployeeScreen(
                        viewModel = empViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // V5: Client Settings → now routes to unified SharedSettingsScreen
                composable(NavRoutes.CLIENT_SETTINGS) {
                    val settingsViewModel: com.duka.settings.shared.SettingsViewModel = hiltViewModel()
                    com.duka.settings.shared.SharedSettingsScreen(
                        role = "client",
                        viewModel = settingsViewModel,
                        businessRepository = businessRepository,
                        onBack = { navController.popBackStack() },
                        onNavigateToSuggestionsHistory = { navController.navigate(NavRoutes.SUGGESTIONS_HISTORY) },
                        onNavigateToDataPrivacy = { navController.navigate(NavRoutes.DATA_PRIVACY) }
                    )
                }

                // V5: Suggestions History
                composable(NavRoutes.SUGGESTIONS_HISTORY) {
                    com.duka.settings.SuggestionsHistoryScreen(
                        clientUserId = sessionState.currentUser?.id ?: 0L,
                        feedbackReportRepository = feedbackReportRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                // V5: Data & Privacy
                composable(NavRoutes.DATA_PRIVACY) {
                    com.duka.settings.DataPrivacyScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                // V5: Employee Settings → now routes to unified SharedSettingsScreen
                composable(NavRoutes.EMPLOYEE_SETTINGS) {
                    val settingsViewModel: com.duka.settings.shared.SettingsViewModel = hiltViewModel()
                    com.duka.settings.shared.SharedSettingsScreen(
                        role = "employee",
                        viewModel = settingsViewModel,
                        businessRepository = businessRepository,
                        employeeRepository = employeeRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                // === V3/V4 ROUTES ===
                // Role-gating: every V3/V4 route checks role before rendering.
                // A CLIENT or EMPLOYEE session must never see GOV routes, and vice versa.

                // V3 Client routes
                composable(NavRoutes.CLIENT_DISCOVER) {
                    // Role gate: only client role allowed
                    if (sessionState.currentRole != "client") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        ClientDiscoverScreen(
                            businessRepository = businessRepository,
                            shopRatingRepository = shopRatingRepository,
                            onBusinessSelected = { bizId ->
                                navController.navigate(NavRoutes.clientStoreRoute(bizId))
                            },
                            onNavigateToSettings = { navController.navigate(NavRoutes.CLIENT_SETTINGS) },
                            onLogout = { sessionViewModel.logout() }
                        )
                    }
                }

                composable(
                    NavRoutes.CLIENT_STORE,
                    arguments = listOf(navArgument("businessId") { type = NavType.LongType })
                ) { backStackEntry ->
                    if (sessionState.currentRole != "client") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        val businessId = backStackEntry.arguments?.getLong("businessId") ?: 0L
                        ClientStoreScreen(
                            businessId = businessId,
                            businessRepository = businessRepository,
                            productRepository = productRepository,
                            saleRepository = saleRepository,
                            purchaseRepository = purchaseRepository,
                            clientUserId = sessionState.currentUser?.id ?: 0L,
                            onBack = { navController.popBackStack() },
                            onPurchaseComplete = { saleId ->
                                navController.navigate(NavRoutes.clientReceiveEbmRoute(saleId))
                            }
                        )
                    }
                }

                composable(
                    NavRoutes.CLIENT_RECEIVE_EBM,
                    arguments = listOf(navArgument("saleId") { type = NavType.LongType }),
                    enterTransition = {
                        scaleIn(tween(300, easing = FastOutSlowInEasing)) + fadeIn(tween(200))
                    },
                    exitTransition = { fadeOut(tween(200)) }
                ) { backStackEntry ->
                    if (sessionState.currentRole != "client") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        val saleId = backStackEntry.arguments?.getLong("saleId") ?: 0L
                        ClientReceiveEbmScreen(
                            saleId = saleId,
                            saleRepository = saleRepository,
                            productRepository = productRepository,
                            ebmGateway = ebmGateway,
                            onDismiss = { navController.popBackStack() }
                        )
                    }
                }

                composable(NavRoutes.CLIENT_BUDGET) {
                    if (sessionState.currentRole != "client") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        val clientUserId = sessionState.currentUser?.id ?: 0L
                        if (clientUserId > 0L) {
                            ClientBudgetScreen(
                                clientUserId = clientUserId,
                                budgetGoalRepository = budgetGoalRepository,
                                purchaseRepository = purchaseRepository,
                                onNavigateToSettings = { navController.navigate(NavRoutes.CLIENT_SETTINGS) },
                                onLogout = { sessionViewModel.logout() }
                            )
                        }
                    }
                }

                composable(NavRoutes.CLIENT_FEEDBACK) {
                    if (sessionState.currentRole != "client") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        val clientUserId = sessionState.currentUser?.id ?: 0L
                        if (clientUserId > 0L) {
                            var anonDefault by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                anonDefault = sessionManager.getAnonymousFeedbackDefault()
                            }
                            ClientFeedbackScreen(
                                clientUserId = clientUserId,
                                feedbackReportRepository = feedbackReportRepository,
                                businessRepository = businessRepository,
                                onBack = { navController.popBackStack() },
                                initialAnonymousDefault = anonDefault
                            )
                        }
                    }
                }

                // V3 Government routes
                composable(NavRoutes.GOV_TAX_ENGINE) {
                    if (sessionState.currentRole != "government_admin") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        GovernmentTaxEngineScreen(
                            businessRepository = businessRepository,
                            saleRepository = saleRepository,
                            purchaseRepository = purchaseRepository
                        )
                    }
                }

                composable(NavRoutes.GOV_SECTOR_VIEW) {
                    if (sessionState.currentRole != "government_admin") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        GovernmentSectorViewScreen(
                            businessRepository = businessRepository,
                            issueRepository = issueRepository,
                            feedbackReportRepository = feedbackReportRepository
                        )
                    }
                }

                // V3 Owner routes (under More)
                composable(NavRoutes.CREDIT_READINESS) {
                    if (sessionState.currentRole != "owner") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        CreditReadinessScreen(
                            businessRepository = businessRepository,
                            saleRepository = saleRepository,
                            ebmRepository = ebmRepository,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                composable(NavRoutes.TRENDING_SHOPS) {
                    TrendingShopsScreen(
                        businessRepository = businessRepository,
                        shopRatingRepository = shopRatingRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                // V4 Supply Network routes
                composable(NavRoutes.REQUEST_RESTOCK) {
                    if (sessionState.currentRole != "owner") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        RequestRestockScreen(
                            businessRepository = businessRepository,
                            productRepository = productRepository,
                            saleRepository = saleRepository,
                            wholesalerRepository = wholesalerRepository,
                            restockRequestRepository = restockRequestRepository,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }

                composable(NavRoutes.WHOLESALER_MARKETPLACE) {
                    WholesalerMarketplaceScreen(
                        wholesalerRepository = wholesalerRepository,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.REGIONAL_ROADMAP) {
                    RegionalRoadmapScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                // === PHASE 3 ROUTES ===
                // Explore — client-facing business discovery (role-gated like other client routes).
                composable(NavRoutes.EXPLORE) {
                    if (sessionState.currentRole != "client") {
                        LaunchedEffect(Unit) { navController.navigate(NavRoutes.LOGIN) { popUpTo(0) { inclusive = true } } }
                    } else {
                        com.duka.phase3.ui.ExploreScreen(
                            repository = phase3Repository,
                            userDistrict = "Kigali", // Phase 3 heuristic default; profile district lands in a later pass
                            onOpenShop = { remoteId ->
                                navController.navigate(NavRoutes.shopProfileRoute(remoteId))
                            }
                        )
                    }
                }

                // Shop Profile — opened from an Explore card.
                composable(
                    NavRoutes.SHOP_PROFILE,
                    arguments = listOf(navArgument("shopRemoteId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val shopRemoteId = backStackEntry.arguments?.getString("shopRemoteId") ?: ""

                    // Phase 4: purchase sheet state, keyed to this shop's products.
                    var purchaseProduct by remember {
                        mutableStateOf<com.duka.phase3.data.RemoteShopProduct?>(null)
                    }
                    val shopForSheet by phase3Repository.observeShop(shopRemoteId)
                        .collectAsState(initial = null)

                    com.duka.phase3.ui.ShopProfileScreen(
                        repository = phase3Repository,
                        shopRemoteId = shopRemoteId,
                        onBack = { navController.popBackStack() },
                        onMessageShop = { id, _ ->
                            navController.navigate(
                                NavRoutes.clientChatRoute(id, sessionState.currentUser?.id ?: 0L, "client")
                            )
                        },
                        onBuyProduct = { product -> purchaseProduct = product }
                    )

                    // Phase 4 purchase sheet — opens from any product card tap.
                    purchaseProduct?.let { product ->
                        com.duka.phase4.ui.ClientPurchaseSheet(
                            product = product,
                            shopRemoteId = shopRemoteId,
                            shopName = shopForSheet?.name ?: "Shop",
                            clientUserId = sessionState.currentUser?.id ?: 0L,
                            service = clientPurchaseSyncService,
                            onDismiss = { purchaseProduct = null },
                            onPurchaseRecorded = { remoteId, _ ->
                                purchaseProduct = null
                                navController.navigate(NavRoutes.clientPurchaseReceiptRoute(remoteId))
                            }
                        )
                    }
                }

                // Client↔Owner chat — reachable from Shop Profile (client side)
                // and from the owner chat list (owner side). The conversation is
                // keyed by (shop, CLIENT user id); senderRole only sets perspective.
                composable(
                    NavRoutes.CLIENT_CHAT,
                    arguments = listOf(
                        navArgument("shopRemoteId") { type = NavType.StringType },
                        navArgument("clientUserId") { type = NavType.LongType },
                        navArgument("senderRole") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val shopRemoteId = backStackEntry.arguments?.getString("shopRemoteId") ?: ""
                    val clientUserId = backStackEntry.arguments?.getLong("clientUserId") ?: 0L
                    val senderRole = backStackEntry.arguments?.getString("senderRole") ?: "client"
                    com.duka.phase3.ui.ClientChatScreen(
                        repository = phase3Repository,
                        service = clientChatService,
                        shopRemoteId = shopRemoteId,
                        clientUserId = clientUserId,
                        senderRole = senderRole,
                        senderName = sessionState.currentUserName.ifBlank { if (senderRole == "client") "Client" else "Owner" },
                        onBack = { navController.popBackStack() }
                    )
                }

                // Owner chat list — every active client conversation.
                composable(NavRoutes.OWNER_CLIENT_CHAT_LIST) {
                    com.duka.phase3.ui.OwnerClientChatListScreen(
                        repository = phase3Repository,
                        service = clientChatService,
                        onBack = { navController.popBackStack() },
                        onOpenConversation = { shopId, _, clientUserId ->
                            navController.navigate(
                                NavRoutes.clientChatRoute(shopId, clientUserId, "owner")
                            )
                        }
                    )
                }

                // === PHASE 4 ROUTES ===
                // Instant EBM receipt after a client purchase (see ClientPurchaseSheet).
                composable(
                    NavRoutes.CLIENT_PURCHASE_RECEIPT,
                    arguments = listOf(navArgument("remoteId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val remoteId = backStackEntry.arguments?.getString("remoteId") ?: ""
                    com.duka.phase4.ui.ClientPurchaseReceiptScreen(
                        purchaseRemoteId = remoteId,
                        service = clientPurchaseSyncService,
                        ebmGateway = ebmGateway,
                        onDone = {
                            navController.navigate(NavRoutes.EXPLORE) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OwnerBottomBar(navController: androidx.navigation.NavController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Products,
        BottomNavItem.Tax,
        BottomNavItem.Chat,
        BottomNavItem.More
    )

    // Animated indicator position
    val density = LocalDensity.current
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    val indicatorOffset by animateDpAsState(
        targetValue = with(density) { (selectedIndex * 72).dp },
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "tabIndicator"
    )

    NavigationBar(
        containerColor = White,
        contentColor = Forest
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(NavRoutes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Forest,
                    selectedTextColor = Forest,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                    indicatorColor = Mist
                )
            )
        }
    }
}

@Composable
fun EmployeeBottomBar(navController: androidx.navigation.NavController, currentRoute: String?) {
    val items = listOf(
        Triple(NavRoutes.EMPLOYEE_SELL, "Sell", Icons.Outlined.Dashboard),
        Triple(NavRoutes.CHAT, "Chat", Icons.Outlined.ChatBubbleOutline)
    )

    // Animated indicator position
    val selectedIndex = items.indexOfFirst { it.first == currentRoute }.coerceAtLeast(0)
    val indicatorOffset by animateDpAsState(
        targetValue = with(LocalDensity.current) { (selectedIndex * 160).dp },
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "tabIndicator"
    )

    NavigationBar(
        containerColor = White,
        contentColor = Forest
    ) {
        items.forEach { (route, label, icon) ->
            val selected = currentRoute == route
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = label
                    )
                },
                label = { Text(label) },
                selected = selected,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(NavRoutes.EMPLOYEE_SELL) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Forest,
                    selectedTextColor = Forest,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                    indicatorColor = Mist
                )
            )
        }
    }
}

// V3/V4: Client bottom bar — now 4 tabs: Discover, Explore (Phase 3), Spending, Feedback
@Composable
fun ClientBottomBar(navController: androidx.navigation.NavController, currentRoute: String?) {
    val items = listOf(
        Triple(NavRoutes.CLIENT_DISCOVER, "Discover", Icons.Outlined.Store),
        Triple(NavRoutes.EXPLORE, "Explore", Icons.Outlined.Explore),
        Triple(NavRoutes.CLIENT_BUDGET, "Spending", Icons.Outlined.AccountBalanceWallet),
        Triple(NavRoutes.CLIENT_FEEDBACK, "Feedback", Icons.Outlined.Feedback)
    )

    NavigationBar(
        containerColor = White,
        contentColor = Forest
    ) {
        items.forEach { (route, label, icon) ->
            val selected = currentRoute == route
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = label
                    )
                },
                label = { Text(label) },
                selected = selected,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(NavRoutes.CLIENT_DISCOVER) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Forest,
                    selectedTextColor = Forest,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                    indicatorColor = Mist
                )
            )
        }
    }
}

// V3/V4: Government Admin bottom bar
@Composable
fun GovAdminBottomBar(navController: androidx.navigation.NavController, currentRoute: String?) {
    val items = listOf(
        Triple(NavRoutes.GOV_TAX_ENGINE, "Tax Engine", Icons.AutoMirrored.Outlined.ReceiptLong),
        Triple(NavRoutes.GOV_SECTOR_VIEW, "Sector View", Icons.Outlined.MoreHoriz)
    )

    NavigationBar(
        containerColor = White,
        contentColor = Forest
    ) {
        items.forEach { (route, label, icon) ->
            val selected = currentRoute == route
            NavigationBarItem(
                icon = {
                    Icon(
                        icon,
                        contentDescription = label
                    )
                },
                label = { Text(label) },
                selected = selected,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(NavRoutes.GOV_TAX_ENGINE) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Forest,
                    selectedTextColor = Forest,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                    indicatorColor = Mist
                )
            )
        }
    }
}
