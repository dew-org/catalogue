package com.dew.catalogue.infrastructure.persistence.mongo

import com.dew.catalogue.domain.CatalogueRepository
import com.dew.catalogue.domain.Product
import com.dew.catalogue.domain.ProductId
import com.dew.common.domain.Price
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.bson.Document
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
class MongoDbCatalogueRepository(
    private val mongoDbConfiguration: MongoDbConfiguration,
    private val mongoClient: MongoClient
) : CatalogueRepository {

    override fun save(product: Product): Mono<Boolean> =
        Mono.from(collection.insertOne(product.toDocument())).map { true }.onErrorReturn(false)

    override fun find(codeOrSku: String): Mono<Product> = Mono.from(
        collection.find(
            Filters.or(Filters.eq("_id.code", codeOrSku), Filters.eq("_id.sku", codeOrSku))
        ).first()
    ).mapNotNull { it.toProduct() }

    override fun searchAll(): Publisher<Product> = Flux.from(collection.find()).map { it.toProduct() }

    private val collection: MongoCollection<Document>
        get() = mongoClient.getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection)

    private fun Product.toDocument(): Document =
        Document("_id", id.toDocument())
            .append("name", name)
            .append("description", description)
            .append("regularPrice", regularPrice.toDocument())
            .append("salePrice", salePrice.toDocument())
            .append("discount", discount)
            .append("tax", tax)
            .append("createdAt", createdAt)


    private fun ProductId.toDocument(): Document =
        Document("code", code).append("sku", sku)

    private fun Price.toDocument(): Document =
        Document("amount", amount).append("currency", currency)

    private fun Document.toProduct(): Product =
        Product(
            get("_id", Document::class.java).toProductId(),
            getString("name"),
            getString("description"),
            get("regularPrice", Document::class.java).toPrice(),
            get("salePrice", Document::class.java).toPrice(),
            getDouble("discount").toFloat(),
            getDouble("tax").toFloat(),
            getDate("createdAt")
        )

    private fun Document.toProductId(): ProductId = ProductId(getString("code"), getString("sku"))

    private fun Document.toPrice(): Price = Price(getDouble("amount").toFloat(), getString("currency"))

}