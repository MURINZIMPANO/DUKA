package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Feedback_reports(
  public val id: Long,
  public val clientUserId: Long,
  public val targetBusinessId: Long,
  public val category: String,
  public val message: String,
  public val isAnonymous: Long,
  public val status: String,
  public val createdAt: Long,
)
