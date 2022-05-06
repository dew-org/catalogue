package com.dew

import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.common.application.create.CreatePriceCommand
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

@MicronautTest
@Testcontainers
class CatalogueControllerSpec extends Specification implements TestPropertyProvider {

    static MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017)

    @Inject
    CatalogueClient client

    def 'test interact with catalogue api'() {
        when:
        var product = new CreateProductCommand(
                "123",
                "123-CEL",
                "Celular",
                "None",
                new CreatePriceCommand(100.0f, "USD"),
                new CreatePriceCommand(150.0f, "USD"),
                0.0f,
                0.0f)
        var status = client.save(product)

        then:
        status == HttpStatus.CREATED

        when:
        var response = client.findByCodeOrSku("123-CEL")

        then:
        response.status == HttpStatus.OK
        response.body.present
        response.body().code == "123"
        response.body().sku == "123-CEL"
        response.body().name == "Celular"
        response.body().regularPrice.amount == 100.0f
        response.body().regularPrice.currency == "USD"

        when:
        response = client.findByCodeOrSku("321")

        then:
        response.status == HttpStatus.NOT_FOUND
        !response.body.present

        when:
        var products = client.searchAll()

        then:
        products.size() == 1

        when:
        client.save(product)

        then:
        thrown(HttpClientException)
    }

    @Override
    Map<String, String> getProperties() {
        mongo.start()

        return ["mongodb.uri": mongo.replicaSetUrl]
    }
}
