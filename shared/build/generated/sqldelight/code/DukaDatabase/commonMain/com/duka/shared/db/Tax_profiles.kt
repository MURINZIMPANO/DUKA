package com.duka.shared.db

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Tax_profiles(
  public val businessId: Long,
  public val quarterlyTurnover: Double,
  public val tier: String,
  public val vatCollected: Double,
)
