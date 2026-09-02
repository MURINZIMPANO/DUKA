package com.duka.shared.db

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Products(
  public val id: Long,
  public val businessId: Long,
  public val name: String,
  public val price: Double,
  public val stockQuantity: Long,
  public val category: String,
  public val createdAt: Long,
  public val isDeleted: Long,
  public val costPrice: Long,
  public val lowStockThreshold: Long,
)
