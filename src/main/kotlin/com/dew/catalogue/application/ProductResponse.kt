package com.dew.catalogue.application

import io.micronaut.core.annotation.Introspected

@Introspected
data class ProductResponse(
    val code: String,
)
