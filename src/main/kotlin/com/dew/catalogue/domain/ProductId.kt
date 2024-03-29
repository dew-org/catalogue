package com.dew.catalogue.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import javax.validation.constraints.NotBlank

@Introspected
@ReflectiveAccess
data class ProductId @Creator @BsonCreator constructor(
    @field:BsonProperty("code") @param:BsonProperty("code") @field:NotBlank val code: String,
    @field:BsonProperty("sku") @param:BsonProperty("sku") @field:NotBlank val sku: String
)

