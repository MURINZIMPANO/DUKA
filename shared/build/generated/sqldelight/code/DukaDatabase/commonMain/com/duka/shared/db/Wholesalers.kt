package com.duka.shared.db

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Wholesalers(
  public val id: Long,
  public val name: String,
  public val specialty: String,
  public val rating: Double,
  public val deliveryEstimate: String,
)
