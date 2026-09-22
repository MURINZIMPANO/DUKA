package com.duka.app.ui.navigation

object NavRoutes {
    // V1/V2 Auth routes
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val COMPANY_SIGNUP = "company_signup"
    const val CLIENT_SIGNUP = "client_signup"
    const val EMPLOYEE_LOGIN = "employee_login"

    // V1/V2 Main routes
    const val DASHBOARD = "dashboard"
    const val PRODUCTS = "products"
    const val ADD_PRODUCT = "add_product"
    const val EMPLOYEE_SELL = "employee_sell"
    const val VOICE_ADD = "voice_add"
    const val TAX_EBM = "tax_ebm"
    const val SEND_EBM = "send_ebm/{saleId}"
    const val CHAT = "chat"
    const val PROMO = "promo"
    const val REPORT_ISSUE = "report_issue"
    const val MORE = "more"
    const val SETTINGS = "settings"

    fun sendEbmRoute(saleId: Long) = "send_ebm/$saleId"

    // V3 Client routes
    const val CLIENT_DISCOVER = "client_discover"
    const val CLIENT_STORE = "client_store/{businessId}"
    const val CLIENT_RECEIVE_EBM = "client_receive_ebm/{saleId}"
    const val CLIENT_BUDGET = "client_budget"
    const val CLIENT_FEEDBACK = "client_feedback"

    fun clientStoreRoute(businessId: Long) = "client_store/$businessId"
    fun clientReceiveEbmRoute(saleId: Long) = "client_receive_ebm/$saleId"

    // V3 Government routes
    const val GOV_TAX_ENGINE = "gov_tax_engine"
    const val GOV_SECTOR_VIEW = "gov_sector_view"

    // V3 Owner routes (under More)
    const val CREDIT_READINESS = "credit_readiness"
    const val TRENDING_SHOPS = "trending_shops"

    // V4 Supply Network routes
    const val REQUEST_RESTOCK = "request_restock"
    const val WHOLESALER_MARKETPLACE = "wholesaler_marketplace"
    const val REGIONAL_ROADMAP = "regional_roadmap"

    // V5 Employee Management routes (Owner portal)
    const val EMPLOYEE_MANAGEMENT = "employee_management"
    const val ADD_EMPLOYEE = "add_employee"

    // V5 Client Settings routes
    const val CLIENT_SETTINGS = "client_settings"
    const val SUGGESTIONS_HISTORY = "suggestions_history"
    const val DATA_PRIVACY = "data_privacy"

    // V5 Employee Settings routes
    const val EMPLOYEE_SETTINGS = "employee_settings"

    // V6 Product Management, Analytics, Intelligence routes
    const val EDIT_PRODUCT = "edit_product/{productId}"
    const val ALERT_CENTRE = "alert_centre"
    const val ANALYTICS_HUB = "analytics_hub"
    const val OWNER_NOTIFICATIONS = "owner_notifications"
    const val CLIENT_NOTIFICATIONS = "client_notifications"
    const val EMPLOYEE_NOTIFICATIONS = "employee_notifications"

    fun editProductRoute(productId: Long) = "edit_product/$productId"

    // === Phase 3 routes (Explore, Shop Profile, client chat) ===
    const val EXPLORE = "explore"
    const val SHOP_PROFILE = "shop_profile/{shopRemoteId}"
    const val CLIENT_CHAT = "client_chat/{shopRemoteId}/{clientUserId}/{senderRole}"
    const val OWNER_CLIENT_CHAT_LIST = "owner_client_chat_list"

    fun shopProfileRoute(shopRemoteId: String) = "shop_profile/$shopRemoteId"
    fun clientChatRoute(shopRemoteId: String, clientUserId: Long, senderRole: String) =
        "client_chat/$shopRemoteId/$clientUserId/$senderRole"

    // === Phase 4 routes (client purchase flow + instant EBM receipt) ===
    const val CLIENT_PURCHASE_RECEIPT = "client_purchase_receipt/{remoteId}"

    fun clientPurchaseReceiptRoute(remoteId: String) = "client_purchase_receipt/$remoteId"
}
