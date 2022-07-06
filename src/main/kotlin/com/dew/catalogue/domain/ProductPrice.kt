package com.dew.catalogue.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

@Introspected
@ReflectiveAccess
data class ProductPrice @Creator @BsonCreator constructor(
    @field:BsonProperty("retailPrice")
    @param:BsonProperty("retailPrice")
    val retailPrice: Double,

    @field:BsonProperty("salePrice")
    @param:BsonProperty("salePrice")
    val salePrice: Double,

    @field:BsonProperty("currency")
    @param:BsonProperty("currency")
    val currency: String
)
