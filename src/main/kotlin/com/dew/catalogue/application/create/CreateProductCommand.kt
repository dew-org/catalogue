package com.dew.catalogue.application.create

import com.dew.common.application.create.CreatePriceCommand
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class CreateProductCommand(
    @field:NotBlank val code: String,
    @field:NotBlank val sku: String,
    @field:NotBlank val name: String,
    var description: String?,
    @field:NotBlank val regularPrice: CreatePriceCommand,
    @field:NotBlank val salePrice: CreatePriceCommand,
    @field:NotBlank val discount: Float,
    @field:NotBlank val tax: Float,
    @field:NotBlank val userId: String,
)
