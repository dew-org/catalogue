package com.dew.catalogue.domain

import com.dew.common.domain.Price
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.Clock
import java.time.Instant
import java.util.Date
import javax.validation.constraints.NotBlank

@Introspected
data class Product @Creator @BsonCreator constructor(
    @field:BsonId @field:BsonProperty("_id")
    @param:BsonProperty("_id")
    val id: ProductId,

    @field:BsonProperty("name")
    @param:BsonProperty("name")
    @field:NotBlank
    val name: String,

    @field:BsonProperty("description")
    @param:BsonProperty("description")
    val description: String?,

    @field:BsonProperty("regularPrice")
    @param:BsonProperty("regularPrice")
    @field:NotBlank
    val regularPrice: Price,

    @field:BsonProperty("salePrice")
    @param:BsonProperty("salePrice")
    @field:NotBlank
    val salePrice: Price,

    @field:BsonProperty("discount")
    @param:BsonProperty("discount")
    val discount: Float,

    @field:BsonProperty("tax")
    @param:BsonProperty("tax")
    val tax: Float,

    @field:BsonProperty("createAt")
    val createdAt: Date = Date.from(Instant.now(Clock.systemUTC()))
) {

    @field:BsonProperty("updateAt")
    var updatedAt: Date? = null
}
