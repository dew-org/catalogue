package com.dew.catalogue.application.update

import com.dew.catalogue.application.create.CreateProductPrice
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class UpdateProductCommand(
    @field:NotBlank val name: String,
    var description: String?,
    @field:NotBlank val price: CreateProductPrice,
    @field:NotBlank val discount: Float,
    @field:NotBlank val tax: Float
)
