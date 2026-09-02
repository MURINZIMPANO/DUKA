package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Businesses(
  public val id: Long,
  public val name: String,
  public val type: String,
  public val employeeCount: Long,
  public val language: String,
  public val district: String,
  public val createdAt: Long,
)
