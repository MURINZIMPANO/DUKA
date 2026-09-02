package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Restock_requests(
  public val id: Long,
  public val businessId: Long,
  public val productId: Long,
  public val wholesalerId: Long,
  public val quantity: Long,
  public val status: String,
  public val requestedAt: Long,
)
