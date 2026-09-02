package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Employees(
  public val id: Long,
  public val businessId: Long,
  public val userId: Long,
  public val code: String,
  public val name: String,
  public val role: String,
  public val isActive: Long,
  public val joinedAt: Long,
  public val lastSeenAt: Long?,
)
