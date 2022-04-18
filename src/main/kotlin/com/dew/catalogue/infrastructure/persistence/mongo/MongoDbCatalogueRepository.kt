package com.dew.catalogue.infrastructure.persistence.mongo

import com.dew.catalogue.domain.CatalogueRepository
import com.dew.catalogue.domain.Product
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Singleton
class MongoDbCatalogueRepository(
    private val mongoDbConfiguration: MongoDbConfiguration, private val mongoClient: MongoClient
) : CatalogueRepository {

    override fun save(product: Product): Mono<Boolean> =
        Mono.from(collection.insertOne(product)).map { true }.onErrorReturn(false)

    override fun find(codeOrSku: String): Mono<Product> = Mono.from(
        collection.find(
            Filters.or(Filters.eq("code", codeOrSku), Filters.eq("sku", codeOrSku))
        ).first()
    )

    override fun searchAll(): Publisher<Product> = collection.find()

    private val collection: MongoCollection<Product>
        get() = mongoClient.getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection, Product::class.java)
}