package com.dew.catalogue.infrastructure.persistence.mongo

import com.dew.catalogue.domain.CatalogueRepository
import com.dew.catalogue.domain.Product
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
class MongoDbCatalogueRepository(
    private val mongoDbConfiguration: MongoDbConfiguration,
    private val mongoClient: MongoClient
) : CatalogueRepository {

    private val logger = LoggerFactory.getLogger(CatalogueRepository::class.java)

    override fun save(product: Product): Mono<Boolean> =
        Mono.from(collection.insertOne(product))
            .map { true }
            .onErrorMap { e ->
                logger.error("Error saving product", e)
                e
            }.onErrorReturn(false)

    override fun find(codeOrSku: String): Mono<Product> = Mono.from(
        collection.find(
            Filters.or(
                Filters.eq("_id.code", codeOrSku),
                Filters.eq("_id.sku", codeOrSku)
            )
        ).first()
    )

    override fun searchAll(userId: String): Publisher<Product> =
        Flux.from(collection.find(Filters.eq("userId", userId)))

    override fun update(product: Product): Mono<Boolean> = Mono.from(
        collection.replaceOne(
            Filters.eq("_id.code", product.id.code), product
        )
    ).map { true }.onErrorReturn(false)

    private val collection: MongoCollection<Product>
        get() = mongoClient.getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection, Product::class.java)
}