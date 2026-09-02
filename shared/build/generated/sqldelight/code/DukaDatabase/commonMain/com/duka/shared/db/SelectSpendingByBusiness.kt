package com.duka.shared.db

import kotlin.Double
import kotlin.Long

public data class SelectSpendingByBusiness(
  public val businessId: Long,
  public val total: Double?,
)
