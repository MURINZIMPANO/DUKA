package com.duka.shared.db

import kotlin.Double
import kotlin.Long

public data class Budget_goals(
  public val id: Long,
  public val clientUserId: Long,
  public val weeklyLimit: Double,
  public val createdAt: Long,
)
