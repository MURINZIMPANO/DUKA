package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Product_alerts(
  public val id: Long,
  public val businessId: Long,
  public val productId: Long,
  public val type: String,
  public val message: String,
  public val severity: String,
  public val isRead: Long,
  public val createdAt: Long,
)
