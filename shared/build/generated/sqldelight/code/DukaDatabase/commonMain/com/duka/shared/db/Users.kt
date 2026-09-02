package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Users(
  public val id: Long,
  public val businessId: Long?,
  public val fullName: String,
  public val phoneOrEmail: String,
  public val passwordHash: String,
  public val role: String,
  public val isDeleted: Long,
  public val createdAt: Long,
)
