package com.dew.catalogue.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.ReflectiveAccess
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.Clock
import java.time.Instant
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
@ReflectiveAccess
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

    @field:BsonProperty("price")
    @param:BsonProperty("price")
    val price: ProductPrice,

    @field:BsonProperty("discount")
    @param:BsonProperty("discount")
    val discount: Float,

    @field:BsonProperty("tax")
    @param:BsonProperty("tax")
    val tax: Float,

    @field:BsonProperty("userId")
    @param:BsonProperty("userId")
    val userId: String,

    @field:BsonProperty("createAt")
    @param:BsonProperty("createAt")
    val createdAt: Date = Date.from(Instant.now(Clock.systemUTC()))
) {

    @field:BsonProperty("updateAt")
    var updatedAt: Date? = null
}
