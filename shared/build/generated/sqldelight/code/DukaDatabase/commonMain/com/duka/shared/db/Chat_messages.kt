package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Chat_messages(
  public val id: Long,
  public val businessId: Long,
  public val senderRole: String,
  public val senderLabel: String,
  public val text: String,
  public val timestamp: Long,
)
