package com.dew.catalogue.application.update

import com.dew.common.application.create.CreatePriceCommand
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class UpdateProductCommand(
    @field:NotBlank val name: String,
    var description: String?,
    @field:NotBlank val regularPrice: CreatePriceCommand,
    @field:NotBlank val salePrice: CreatePriceCommand,
    @field:NotBlank val discount: Float,
    @field:NotBlank val tax: Float
)
