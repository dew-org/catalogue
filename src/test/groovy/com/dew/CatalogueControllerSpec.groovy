package com.dew

import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.catalogue.application.update.UpdateProductCommand
import com.dew.common.application.create.CreatePriceCommand
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest
@Testcontainers
class CatalogueControllerSpec extends Specification implements TestPropertyProvider {

    @Shared
    @AutoCleanup
    static MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017)

    @Inject
    CatalogueClient client

    def 'test interact with catalogue api'() {
        when: 'send create product request'
        var product = new CreateProductCommand(
                "123",
                "123-CEL",
                "Celular",
                "None",
                new CreatePriceCommand(100.0f, "USD"),
                new CreatePriceCommand(150.0f, "USD"),
                0.0f,
                0.0f,
            "user-01")
        var status = client.save(product)

        then: 'should return status 201'
        status == HttpStatus.CREATED

        when: 'try find product by id'
        var response = client.findByCodeOrSku("123")

        then: 'should return status 200'
        response.status == HttpStatus.OK
        response.body.present
        response.body().code == "123"
        response.body().sku == "123-CEL"
        response.body().name == "Celular"
        response.body().regularPrice.amount == 100.0f
        response.body().regularPrice.currency == "USD"

        when: 'try find product by id'
        response = client.findByCodeOrSku("321")

        then: 'should return status 404'
        response.status == HttpStatus.NOT_FOUND
        !response.body.present

        when: 'fetch all products'
        var products = client.searchAll("user-01")

        then: 'should return all products'
        products.size() == 1

        when: 'fetch all products'
        products = client.searchAll("user-02")
        then: 'should return status 200'

        then: 'should return 0 products'
        products.size() == 0

        when: 'send same create product request'
        client.save(product)

        then: 'should return status 409'
        thrown(HttpClientException)

        when: 'try update product'
        var updateProductCommand = new UpdateProductCommand(
                "Smartphone",
                "None",
                new CreatePriceCommand(100.0f, "USD"),
                new CreatePriceCommand(150.0f, "USD"),
                30.0f,
                0.0f
        )
        response = client.update("123", updateProductCommand)

        then: 'should return status 200'
        response.status == HttpStatus.OK

        when: 'try update product'
        response = client.update("321", updateProductCommand)

        then: 'should return status 404'
        response.status == HttpStatus.NOT_FOUND

        when: 'try find updated product by id'
        response = client.findByCodeOrSku("123")

        then: 'should return status 200'
        response.status == HttpStatus.OK
        response.body.present
        response.body().code == "123"
        response.body().sku == "123-CEL"
        response.body().name == "Smartphone"
        response.body().discount == 0.3f
        response.body().regularPrice.amount == 100.0f
        response.body().regularPrice.currency == "USD"
    }

    @Override
    Map<String, String> getProperties() {
        mongo.start()

        return ["mongodb.uri": mongo.replicaSetUrl]
    }
}
