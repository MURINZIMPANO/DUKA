package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class App_notifications(
  public val id: Long,
  public val userId: Long,
  public val businessId: Long,
  public val type: String,
  public val title: String,
  public val body: String,
  public val actionRoute: String,
  public val isRead: Long,
  public val createdAt: Long,
)
