package com.dew

import com.dew.catalogue.application.create.CreateProductCommand
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CatalogueControllerTest : TestPropertyProvider {

    @Inject
    lateinit var catalogueClient: CatalogueClient

    @Test
    fun save_product_should_return_created() {
        val product = CreateProductCommand(
            "123",
            "123-CEL",
            "Celular",
            "None",
            100.0f,
            150.0f,
            0.0f,
            0.0f
        )

        val status = catalogueClient.save(product)

        assertEquals(HttpStatus.CREATED, status)
    }

    override fun getProperties(): Map<String, String> {
        MongoDbUtils.startMongoDb()
        return mapOf("mongodb.uri" to MongoDbUtils.mongoDbUri)
    }

    @AfterAll
    fun cleanup() {
        MongoDbUtils.closeMongoDb()
    }
}