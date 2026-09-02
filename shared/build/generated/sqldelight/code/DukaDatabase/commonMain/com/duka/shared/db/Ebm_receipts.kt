package com.duka.shared.db

import kotlin.Long
import kotlin.String

public data class Ebm_receipts(
  public val id: Long,
  public val saleId: Long,
  public val receiptNumber: String,
  public val status: String,
  public val sentAt: Long,
)
