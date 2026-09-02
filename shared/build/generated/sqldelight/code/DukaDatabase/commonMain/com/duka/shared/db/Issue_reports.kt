package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Issue_reports(
  public val id: Long,
  public val businessId: Long,
  public val category: String,
  public val message: String,
  public val attachBusinessId: Long,
  public val status: String,
  public val createdAt: Long,
)
