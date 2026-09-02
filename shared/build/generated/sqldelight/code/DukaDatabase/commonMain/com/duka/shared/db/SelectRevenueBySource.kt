package com.duka.shared.db

import kotlin.Double
import kotlin.String

public data class SelectRevenueBySource(
  public val source: String,
  public val total: Double?,
)
