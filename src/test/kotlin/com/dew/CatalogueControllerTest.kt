package com.dew

import com.dew.catalogue.application.create.CreateProductCommand
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CatalogueControllerTest : TestPropertyProvider {

    @Container
    val mongo: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest")).withExposedPorts(27017)

    @Inject
    lateinit var catalogueClient: CatalogueClient

    @Test
    fun interact_with_catalogue() {
        var product = CreateProductCommand(
            "123", "123-CEL", "Celular", "None", 100.0f, 150.0f, 0.0f, 0.0f
        )

        val createStatus = catalogueClient.save(product)

        assertEquals(HttpStatus.CREATED, createStatus)

        val response = catalogueClient.findByCodeOrSku("123")

        assertEquals(HttpStatus.OK, response.status)
        assertEquals("123", response.body()?.code)

        val products = catalogueClient.searchAll()

        assertEquals(1, products.size)

        try {
            product = CreateProductCommand(
                "123", "123-CEL", "Celular", "None", 100.0f, 150.0f, 0.0f, 0.0f
            )

            catalogueClient.save(product)

        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.CONFLICT, e.status)
        }
    }

    override fun getProperties(): Map<String, String> {
        mongo.start()

        return mapOf("mongodb.uri" to mongo.replicaSetUrl)
    }
}