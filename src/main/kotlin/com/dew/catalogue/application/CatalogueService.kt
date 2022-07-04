package com.dew.catalogue.application

import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.catalogue.application.update.UpdateProductCommand
import com.dew.catalogue.domain.CatalogueRepository
import com.dew.catalogue.domain.Product
import com.dew.catalogue.domain.ProductId
import com.dew.common.application.PriceResponse
import com.dew.common.domain.Price
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
class CatalogueService(private val catalogueRepository: CatalogueRepository) {

    fun save(request: CreateProductCommand): Mono<Boolean> {
        val product = Product(
            ProductId(request.code, request.sku),
            request.name,
            request.description,
            Price(request.regularPrice.amount, request.regularPrice.currency),
            Price(request.salePrice.amount, request.salePrice.currency),
            request.discount / 100.0f,
            request.tax / 100.0f,
            request.userId
        )

        return catalogueRepository.save(product)
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
                Price(command.regularPrice.amount, command.regularPrice.currency),
                Price(command.salePrice.amount, command.salePrice.currency),
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
            PriceResponse(regularPrice.amount, regularPrice.currency),
            PriceResponse(salePrice.amount, salePrice.currency),
            discount,
            tax,
            createdAt,
            updatedAt
        )
    }
}