package com.duka.shared.db

import kotlin.Double
import kotlin.String

public data class SelectRevenueByCategory(
  public val category: String,
  public val total: Double?,
)
