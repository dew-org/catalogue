package com.dew.catalogue.application

import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.catalogue.application.update.UpdateProductCommand
import com.dew.catalogue.domain.CatalogueRepository
import com.dew.catalogue.domain.Product
import com.dew.catalogue.domain.ProductId
import com.dew.catalogue.domain.ProductPrice
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
class CatalogueService(private val catalogueRepository: CatalogueRepository) {

    private val logger = LoggerFactory.getLogger(CatalogueService::class.java)

    fun save(request: CreateProductCommand): Mono<Boolean> {
        val product = Product(
            ProductId(request.code, request.sku),
            request.name,
            request.description,
            ProductPrice(request.price.retailPrice, request.price.salePrice, request.price.currency),
            request.discount / 100.0f,
            request.tax / 100.0f,
            request.userId
        )

        return catalogueRepository.save(product)
            .onErrorMap { e ->
                logger.error("Error saving product", e)
                e
            }.onErrorReturn(false)
    }

    fun find(codeOrSku: String): Mono<ProductResponse> {
        return catalogueRepository.find(codeOrSku).mapNotNull { it.toResponse() }
    }

    fun searchAll(userId: String): Publisher<ProductResponse> {
        return Flux.from(catalogueRepository.searchAll(userId)).map { it.toResponse() }
    }

    fun update(code: String, command: UpdateProductCommand): Mono<Boolean> {
        return catalogueRepository.find(code).flatMap {
            val productToUpdate = Product(
                it.id,
                command.name,
                command.description,
                ProductPrice(command.price.retailPrice, command.price.salePrice, command.price.currency),
                command.discount / 100.0f,
                command.tax / 100.0f,
                it.userId
            )
            catalogueRepository.update(productToUpdate)
        }
    }

    private fun Product.toResponse(): ProductResponse {
        return ProductResponse(
            id.code,
            id.sku,
            name,
            description,
            ProductPriceResponse(price.retailPrice, price.salePrice, price.currency),
            discount,
            tax,
            createdAt,
            updatedAt
        )
    }
}