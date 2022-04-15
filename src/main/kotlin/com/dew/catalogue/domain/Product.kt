package com.dew.catalogue.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.Clock
import java.time.Instant
import java.util.Date
import javax.validation.constraints.NotBlank

@Introspected
data class Product @Creator @BsonCreator constructor(
    @field:BsonProperty("code") @param:BsonProperty("code") @field:NotBlank val code: String,
    @field:BsonProperty("sku") @param:BsonProperty("sku") @field:NotBlank val sku: String,
    @field:BsonProperty("name") @param:BsonProperty("name") @field:NotBlank val name: String,
    @field:BsonProperty("description") @param:BsonProperty("description") val description: String?,
    @field:BsonProperty("regularPrice") @param:BsonProperty("regularPrice") @field:NotBlank val regularPrice: Float,
    @field:BsonProperty("salePrice") @param:BsonProperty("salePrice") @field:NotBlank val salePrice: Float,
    @field:BsonProperty("discount") @param:BsonProperty("discount") val discount: Float,
    @field:BsonProperty("tax") @param:BsonProperty("tax") val tax: Float,
) {
    @field:BsonProperty("createAt")
    val createAt: Date = Date.from(Instant.now(Clock.systemUTC()))

    @field:BsonProperty("updateAt")
    var updateAt: Date? = null
}
