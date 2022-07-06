package com.dew.catalogue.application

import io.micronaut.core.annotation.Introspected
import java.util.Date

@Introspected
data class ProductResponse(
    val code: String,
    val sku: String,
    val name: String,
    val description: String?,
    val price: ProductPriceResponse,
    val discount: Float,
    val tax: Float,
    val createdAt: Date,
    val updatedAt: Date?
)
