package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Promos(
  public val id: Long,
  public val businessId: Long,
  public val title: String,
  public val discountPercent: Long,
  public val startDate: Long,
  public val endDate: Long,
  public val isLive: Long,
)
