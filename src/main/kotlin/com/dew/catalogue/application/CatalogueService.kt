package com.dew.catalogue.application

import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.catalogue.domain.CatalogueRepository
import com.dew.catalogue.domain.Product
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Singleton
class CatalogueService(private val catalogueRepository: CatalogueRepository) {

    fun save(request: CreateProductCommand): Mono<Boolean> {
        val product = Product(
            request.code,
            request.sku,
            request.name,
            request.description,
            request.regularPrice,
            request.salePrice,
            request.discount / 100.0f,
            request.tax / 100.0f
        )

        return catalogueRepository.save(product)
    }

    fun find(codeOrSku: String): Mono<ProductResponse> {
        return catalogueRepository.find(codeOrSku).mapNotNull { product ->
            ProductResponse(
                product.code,
                product.sku,
                product.name,
                product.description,
                product.regularPrice,
                product.salePrice,
                product.discount,
                product.tax,
                product.createdAt,
                product.updatedAt
            )
        }
    }

    fun searchAll(): Publisher<ProductResponse> {
        return Flux.from(catalogueRepository.searchAll()).map { product ->
            ProductResponse(
                product.code,
                product.sku,
                product.name,
                product.description,
                product.regularPrice,
                product.salePrice,
                product.discount,
                product.tax,
                product.createdAt,
                product.updatedAt
            )
        }
    }
}