package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Stock_adjustments(
  public val id: Long,
  public val productId: Long,
  public val businessId: Long,
  public val delta: Long,
  public val reason: String,
  public val timestamp: Long,
)
