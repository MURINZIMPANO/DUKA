package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Expenses(
  public val id: Long,
  public val businessId: Long,
  public val label: String,
  public val amount: Long,
  public val periodStart: Long,
  public val periodEnd: Long,
  public val createdAt: Long,
)
