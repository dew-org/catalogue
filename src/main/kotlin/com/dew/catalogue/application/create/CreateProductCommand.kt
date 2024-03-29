package com.dew.catalogue.application.create

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class CreateProductCommand(
    @field:NotBlank val code: String,
    @field:NotBlank val sku: String,
    @field:NotBlank val name: String,
    var description: String?,
    @field:NotBlank val price: CreateProductPrice,
    @field:NotBlank val discount: Float,
    @field:NotBlank val tax: Float,
    @field:NotBlank val userId: String,
)
