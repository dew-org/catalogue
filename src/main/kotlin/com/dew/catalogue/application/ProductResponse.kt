package com.dew.catalogue.application

import io.micronaut.core.annotation.Introspected
import java.util.Date

@Introspected
data class ProductResponse(
    val code: String,
    val sku: String,
    val name: String,
    val description: String?,
    val regularPrice: Float,
    val salePrice: Float,
    val discount: Float,
    val tax: Float,
    val createdAt: Date,
    val updatedAt: Date?
)
