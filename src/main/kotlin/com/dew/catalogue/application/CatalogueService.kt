package com.dew.catalogue.application

import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.catalogue.domain.CatalogueRepository
import com.dew.catalogue.domain.Product
import jakarta.inject.Singleton
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
        return catalogueRepository.find(codeOrSku).mapNotNull { product -> ProductResponse(product.code) }
    }
}