package com.duka.android.data.mappers

// Room entities
import com.duka.app.data.local.entity.Business as RoomBusiness
import com.duka.app.data.local.entity.Product as RoomProduct
import com.duka.app.data.local.entity.Sale as RoomSale
import com.duka.app.data.local.entity.Employee as RoomEmployee
import com.duka.app.data.local.entity.User as RoomUser
import com.duka.app.data.local.entity.ChatMessage as RoomChatMessage
import com.duka.app.data.local.entity.Promo as RoomPromo
import com.duka.app.data.local.entity.IssueReport as RoomIssueReport
import com.duka.app.data.local.entity.EbmReceipt as RoomEbmReceipt
import com.duka.app.data.local.entity.TaxProfile as RoomTaxProfile
import com.duka.app.data.local.entity.Purchase as RoomPurchase
import com.duka.app.data.local.entity.BudgetGoal as RoomBudgetGoal
import com.duka.app.data.local.entity.FeedbackReport as RoomFeedbackReport
import com.duka.app.data.local.entity.ShopRating as RoomShopRating
import com.duka.app.data.local.entity.Wholesaler as RoomWholesaler
import com.duka.app.data.local.entity.RestockRequest as RoomRestockRequest
import com.duka.app.data.local.entity.StockAdjustment as RoomStockAdjustment
import com.duka.app.data.local.entity.ProductAlert as RoomProductAlert
import com.duka.app.data.local.entity.AppNotification as RoomAppNotification
import com.duka.app.data.local.entity.Expense as RoomExpense

// Shared domain models
import com.duka.shared.domain.Business as SharedBusiness
import com.duka.shared.domain.Product as SharedProduct
import com.duka.shared.domain.Sale as SharedSale
import com.duka.shared.domain.Employee as SharedEmployee
import com.duka.shared.domain.User as SharedUser
import com.duka.shared.domain.ChatMessage as SharedChatMessage
import com.duka.shared.domain.Promo as SharedPromo
import com.duka.shared.domain.IssueReport as SharedIssueReport
import com.duka.shared.domain.EbmReceipt as SharedEbmReceipt
import com.duka.shared.domain.TaxProfile as SharedTaxProfile
import com.duka.shared.domain.Purchase as SharedPurchase
import com.duka.shared.domain.BudgetGoal as SharedBudgetGoal
import com.duka.shared.domain.FeedbackReport as SharedFeedbackReport
import com.duka.shared.domain.ShopRating as SharedShopRating
import com.duka.shared.domain.Wholesaler as SharedWholesaler
import com.duka.shared.domain.RestockRequest as SharedRestockRequest
import com.duka.shared.domain.StockAdjustment as SharedStockAdjustment
import com.duka.shared.domain.ProductAlert as SharedProductAlert
import com.duka.shared.domain.AppNotification as SharedAppNotification
import com.duka.shared.domain.Expense as SharedExpense
import com.duka.shared.domain.ProductCount as SharedProductCount
import com.duka.shared.domain.CategoryRevenue as SharedCategoryRevenue
import com.duka.shared.domain.SourceRevenue as SharedSourceRevenue
import com.duka.shared.domain.BusinessSpend as SharedBusinessSpend

// Room DAO data classes
import com.duka.app.data.local.dao.ProductCount as RoomProductCount
import com.duka.app.data.local.dao.CategoryRevenue as RoomCategoryRevenue
import com.duka.app.data.local.dao.SourceRevenue as RoomSourceRevenue
import com.duka.app.data.local.dao.BusinessSpend as RoomBusinessSpend

// ──────────────────────────────────────────────────
// Core entity mappers — Room ↔ Shared
// ──────────────────────────────────────────────────

fun RoomBusiness.toShared(): SharedBusiness = SharedBusiness(
    id = id, name = name, type = type,
    employeeCount = employeeCount, language = language,
    district = district, createdAt = createdAt
)

fun SharedBusiness.toRoom(): RoomBusiness = RoomBusiness(
    id = id, name = name, type = type,
    employeeCount = employeeCount, language = language,
    district = district, createdAt = createdAt
)

fun RoomProduct.toShared(): SharedProduct = SharedProduct(
    id = id, businessId = businessId, name = name,
    price = price, stockQuantity = stockQuantity,
    category = category, createdAt = createdAt,
    isDeleted = isDeleted, costPrice = costPrice,
    lowStockThreshold = lowStockThreshold
)

fun SharedProduct.toRoom(): RoomProduct = RoomProduct(
    id = id, businessId = businessId, name = name,
    price = price, stockQuantity = stockQuantity,
    category = category, createdAt = createdAt,
    isDeleted = isDeleted, costPrice = costPrice,
    lowStockThreshold = lowStockThreshold
)

fun RoomSale.toShared(): SharedSale = SharedSale(
    id = id, productId = productId, businessId = businessId,
    amount = amount, timestamp = timestamp, source = source
)

fun SharedSale.toRoom(): RoomSale = RoomSale(
    id = id, productId = productId, businessId = businessId,
    amount = amount, timestamp = timestamp, source = source
)

fun RoomUser.toShared(): SharedUser = SharedUser(
    id = id, businessId = businessId, fullName = fullName,
    phoneOrEmail = phoneOrEmail, passwordHash = passwordHash,
    role = role, isDeleted = isDeleted, createdAt = createdAt
)

fun SharedUser.toRoom(): RoomUser = RoomUser(
    id = id, businessId = businessId, fullName = fullName,
    phoneOrEmail = phoneOrEmail, passwordHash = passwordHash,
    role = role, isDeleted = isDeleted, createdAt = createdAt
)

fun RoomEmployee.toShared(): SharedEmployee = SharedEmployee(
    id = id, businessId = businessId, userId = userId,
    code = code, name = name, role = role,
    isActive = isActive, joinedAt = joinedAt,
    lastSeenAt = lastSeenAt
)

fun SharedEmployee.toRoom(): RoomEmployee = RoomEmployee(
    id = id, businessId = businessId, userId = userId,
    code = code, name = name, role = role,
    isActive = isActive, joinedAt = joinedAt,
    lastSeenAt = lastSeenAt
)

fun RoomChatMessage.toShared(): SharedChatMessage = SharedChatMessage(
    id = id, businessId = businessId, senderRole = senderRole,
    senderLabel = senderLabel, text = text, timestamp = timestamp
)

fun SharedChatMessage.toRoom(): RoomChatMessage = RoomChatMessage(
    id = id, businessId = businessId, senderRole = senderRole,
    senderLabel = senderLabel, text = text, timestamp = timestamp
)

fun RoomPromo.toShared(): SharedPromo = SharedPromo(
    id = id, businessId = businessId, title = title,
    discountPercent = discountPercent, startDate = startDate,
    endDate = endDate, isLive = isLive
)

fun SharedPromo.toRoom(): RoomPromo = RoomPromo(
    id = id, businessId = businessId, title = title,
    discountPercent = discountPercent, startDate = startDate,
    endDate = endDate, isLive = isLive
)

fun RoomIssueReport.toShared(): SharedIssueReport = SharedIssueReport(
    id = id, businessId = businessId, category = category,
    message = message, attachBusinessId = attachBusinessId,
    status = status, createdAt = createdAt
)

fun SharedIssueReport.toRoom(): RoomIssueReport = RoomIssueReport(
    id = id, businessId = businessId, category = category,
    message = message, attachBusinessId = attachBusinessId,
    status = status, createdAt = createdAt
)

fun RoomEbmReceipt.toShared(): SharedEbmReceipt = SharedEbmReceipt(
    id = id, saleId = saleId, receiptNumber = receiptNumber,
    status = status, sentAt = sentAt
)

fun SharedEbmReceipt.toRoom(): RoomEbmReceipt = RoomEbmReceipt(
    id = id, saleId = saleId, receiptNumber = receiptNumber,
    status = status, sentAt = sentAt
)

fun RoomTaxProfile.toShared(): SharedTaxProfile = SharedTaxProfile(
    businessId = businessId, quarterlyTurnover = quarterlyTurnover,
    tier = tier, vatCollected = vatCollected
)

fun SharedTaxProfile.toRoom(): RoomTaxProfile = RoomTaxProfile(
    businessId = businessId, quarterlyTurnover = quarterlyTurnover,
    tier = tier, vatCollected = vatCollected
)

// ──────────────────────────────────────────────────
// V3/V4 entity mappers
// ──────────────────────────────────────────────────

fun RoomPurchase.toShared(): SharedPurchase = SharedPurchase(
    id = id, clientUserId = clientUserId, businessId = businessId,
    productId = productId, amount = amount, timestamp = timestamp,
    receiptNumber = receiptNumber
)

fun SharedPurchase.toRoom(): RoomPurchase = RoomPurchase(
    id = id, clientUserId = clientUserId, businessId = businessId,
    productId = productId, amount = amount, timestamp = timestamp,
    receiptNumber = receiptNumber
)

fun RoomBudgetGoal.toShared(): SharedBudgetGoal = SharedBudgetGoal(
    id = id, clientUserId = clientUserId,
    weeklyLimit = weeklyLimit, createdAt = createdAt
)

fun SharedBudgetGoal.toRoom(): RoomBudgetGoal = RoomBudgetGoal(
    id = id, clientUserId = clientUserId,
    weeklyLimit = weeklyLimit, createdAt = createdAt
)

fun RoomFeedbackReport.toShared(): SharedFeedbackReport = SharedFeedbackReport(
    id = id, clientUserId = clientUserId, targetBusinessId = targetBusinessId,
    category = category, message = message, isAnonymous = isAnonymous,
    status = status, createdAt = createdAt
)

fun SharedFeedbackReport.toRoom(): RoomFeedbackReport = RoomFeedbackReport(
    id = id, clientUserId = clientUserId, targetBusinessId = targetBusinessId,
    category = category, message = message, isAnonymous = isAnonymous,
    status = status, createdAt = createdAt
)

fun RoomShopRating.toShared(): SharedShopRating = SharedShopRating(
    id = id, businessId = businessId,
    averageRating = averageRating, ratingCount = ratingCount
)

fun SharedShopRating.toRoom(): RoomShopRating = RoomShopRating(
    id = id, businessId = businessId,
    averageRating = averageRating, ratingCount = ratingCount
)

fun RoomWholesaler.toShared(): SharedWholesaler = SharedWholesaler(
    id = id, name = name, specialty = specialty,
    rating = rating, deliveryEstimate = deliveryEstimate
)

fun SharedWholesaler.toRoom(): RoomWholesaler = RoomWholesaler(
    id = id, name = name, specialty = specialty,
    rating = rating, deliveryEstimate = deliveryEstimate
)

fun RoomRestockRequest.toShared(): SharedRestockRequest = SharedRestockRequest(
    id = id, businessId = businessId, productId = productId,
    wholesalerId = wholesalerId, quantity = quantity,
    status = status, requestedAt = requestedAt
)

fun SharedRestockRequest.toRoom(): RoomRestockRequest = RoomRestockRequest(
    id = id, businessId = businessId, productId = productId,
    wholesalerId = wholesalerId, quantity = quantity,
    status = status, requestedAt = requestedAt
)

// ──────────────────────────────────────────────────
// V5 entity mappers
// ──────────────────────────────────────────────────

fun RoomStockAdjustment.toShared(): SharedStockAdjustment = SharedStockAdjustment(
    id = id, productId = productId, businessId = businessId,
    delta = delta, reason = reason, timestamp = timestamp
)

fun SharedStockAdjustment.toRoom(): RoomStockAdjustment = RoomStockAdjustment(
    id = id, productId = productId, businessId = businessId,
    delta = delta, reason = reason, timestamp = timestamp
)

fun RoomProductAlert.toShared(): SharedProductAlert = SharedProductAlert(
    id = id, businessId = businessId, productId = productId,
    type = type, message = message, severity = severity,
    isRead = isRead, createdAt = createdAt
)

fun SharedProductAlert.toRoom(): RoomProductAlert = RoomProductAlert(
    id = id, businessId = businessId, productId = productId,
    type = type, message = message, severity = severity,
    isRead = isRead, createdAt = createdAt
)

fun RoomAppNotification.toShared(): SharedAppNotification = SharedAppNotification(
    id = id, userId = userId, businessId = businessId,
    type = type, title = title, body = body,
    actionRoute = actionRoute, isRead = isRead,
    createdAt = createdAt
)

fun SharedAppNotification.toRoom(): RoomAppNotification = RoomAppNotification(
    id = id, userId = userId, businessId = businessId,
    type = type, title = title, body = body,
    actionRoute = actionRoute, isRead = isRead,
    createdAt = createdAt
)

fun RoomExpense.toShared(): SharedExpense = SharedExpense(
    id = id, businessId = businessId, label = label,
    amount = amount, periodStart = periodStart,
    periodEnd = periodEnd, createdAt = createdAt
)

fun SharedExpense.toRoom(): RoomExpense = RoomExpense(
    id = id, businessId = businessId, label = label,
    amount = amount, periodStart = periodStart,
    periodEnd = periodEnd, createdAt = createdAt
)

// ──────────────────────────────────────────────────
// DAO data class mappers (ProductCount, CategoryRevenue, etc.)
// ──────────────────────────────────────────────────

fun RoomProductCount.toShared(): SharedProductCount = SharedProductCount(
    productId = productId, cnt = cnt
)

fun RoomCategoryRevenue.toShared(): SharedCategoryRevenue = SharedCategoryRevenue(
    category = category, total = total
)

fun RoomSourceRevenue.toShared(): SharedSourceRevenue = SharedSourceRevenue(
    source = source, total = total
)

fun RoomBusinessSpend.toShared(): SharedBusinessSpend = SharedBusinessSpend(
    businessId = businessId, total = total
)
