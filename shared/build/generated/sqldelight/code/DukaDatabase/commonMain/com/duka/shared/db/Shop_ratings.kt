package com.duka.shared.db

import kotlin.Double
import kotlin.Long

public data class Shop_ratings(
  public val id: Long,
  public val businessId: Long,
  public val averageRating: Double,
  public val ratingCount: Long,
)
