package com.dew.catalogue.application

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class ProductPriceResponse(
    @field:NotBlank
    val retailPrice: Double,

    @field:NotBlank
    val salePrice: Double,

    @field:NotBlank
    val currency: String
)
