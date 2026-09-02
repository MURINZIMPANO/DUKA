package com.duka.shared.db.shared

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.duka.shared.db.DukaDatabase
import com.duka.shared.db.DukaQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<DukaDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = DukaDatabaseImpl.Schema

internal fun KClass<DukaDatabase>.newInstance(driver: SqlDriver): DukaDatabase =
    DukaDatabaseImpl(driver)

private class DukaDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), DukaDatabase {
  override val dukaQueries: DukaQueries = DukaQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE businesses (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    name TEXT NOT NULL,
          |    type TEXT NOT NULL,
          |    employeeCount INTEGER NOT NULL,
          |    language TEXT NOT NULL,
          |    district TEXT NOT NULL,
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE products (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    name TEXT NOT NULL,
          |    price REAL NOT NULL,
          |    stockQuantity INTEGER NOT NULL,
          |    category TEXT NOT NULL,
          |    createdAt INTEGER NOT NULL,
          |    isDeleted INTEGER NOT NULL DEFAULT 0,
          |    costPrice INTEGER NOT NULL DEFAULT 0,
          |    lowStockThreshold INTEGER NOT NULL DEFAULT 5
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE sales (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    productId INTEGER NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    amount REAL NOT NULL,
          |    timestamp INTEGER NOT NULL,
          |    source TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE employees (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    userId INTEGER NOT NULL DEFAULT 0,
          |    code TEXT NOT NULL,
          |    name TEXT NOT NULL,
          |    role TEXT NOT NULL DEFAULT 'cashier',
          |    isActive INTEGER NOT NULL DEFAULT 1,
          |    joinedAt INTEGER NOT NULL,
          |    lastSeenAt INTEGER
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE chat_messages (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    senderRole TEXT NOT NULL,
          |    senderLabel TEXT NOT NULL,
          |    text TEXT NOT NULL,
          |    timestamp INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE promos (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    title TEXT NOT NULL,
          |    discountPercent INTEGER NOT NULL,
          |    startDate INTEGER NOT NULL,
          |    endDate INTEGER NOT NULL,
          |    isLive INTEGER NOT NULL DEFAULT 1
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE issue_reports (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    category TEXT NOT NULL,
          |    message TEXT NOT NULL,
          |    attachBusinessId INTEGER NOT NULL,
          |    status TEXT NOT NULL DEFAULT 'Under review',
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE ebm_receipts (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    saleId INTEGER NOT NULL,
          |    receiptNumber TEXT NOT NULL,
          |    status TEXT NOT NULL,
          |    sentAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE users (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER,
          |    fullName TEXT NOT NULL,
          |    phoneOrEmail TEXT NOT NULL,
          |    passwordHash TEXT NOT NULL,
          |    role TEXT NOT NULL,
          |    isDeleted INTEGER NOT NULL DEFAULT 0,
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE tax_profiles (
          |    businessId INTEGER PRIMARY KEY NOT NULL,
          |    quarterlyTurnover REAL NOT NULL,
          |    tier TEXT NOT NULL,
          |    vatCollected REAL NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE purchases (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    clientUserId INTEGER NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    productId INTEGER NOT NULL,
          |    amount REAL NOT NULL,
          |    timestamp INTEGER NOT NULL,
          |    receiptNumber TEXT NOT NULL DEFAULT ''
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE budget_goals (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    clientUserId INTEGER NOT NULL,
          |    weeklyLimit REAL NOT NULL,
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE feedback_reports (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    clientUserId INTEGER NOT NULL,
          |    targetBusinessId INTEGER NOT NULL,
          |    category TEXT NOT NULL,
          |    message TEXT NOT NULL,
          |    isAnonymous INTEGER NOT NULL DEFAULT 0,
          |    status TEXT NOT NULL DEFAULT 'Under review',
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE shop_ratings (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    averageRating REAL NOT NULL,
          |    ratingCount INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE wholesalers (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    name TEXT NOT NULL,
          |    specialty TEXT NOT NULL,
          |    rating REAL NOT NULL,
          |    deliveryEstimate TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE restock_requests (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    productId INTEGER NOT NULL,
          |    wholesalerId INTEGER NOT NULL,
          |    quantity INTEGER NOT NULL,
          |    status TEXT NOT NULL DEFAULT 'requested',
          |    requestedAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE stock_adjustments (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    productId INTEGER NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    delta INTEGER NOT NULL,
          |    reason TEXT NOT NULL,
          |    timestamp INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE product_alerts (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    productId INTEGER NOT NULL,
          |    type TEXT NOT NULL,
          |    message TEXT NOT NULL,
          |    severity TEXT NOT NULL,
          |    isRead INTEGER NOT NULL DEFAULT 0,
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE app_notifications (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    userId INTEGER NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    type TEXT NOT NULL,
          |    title TEXT NOT NULL,
          |    body TEXT NOT NULL,
          |    actionRoute TEXT NOT NULL,
          |    isRead INTEGER NOT NULL DEFAULT 0,
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE expenses (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
          |    businessId INTEGER NOT NULL,
          |    label TEXT NOT NULL,
          |    amount INTEGER NOT NULL,
          |    periodStart INTEGER NOT NULL,
          |    periodEnd INTEGER NOT NULL,
          |    createdAt INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
