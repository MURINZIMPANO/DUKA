package com.duka.shared.db

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Purchases(
  public val id: Long,
  public val clientUserId: Long,
  public val businessId: Long,
  public val productId: Long,
  public val amount: Double,
  public val timestamp: Long,
  public val receiptNumber: String,
)
