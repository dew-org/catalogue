package com.dew

import com.dew.catalogue.application.CatalogueService
import com.dew.catalogue.application.ProductResponse
import com.dew.catalogue.application.create.CreateProductCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller("/catalogue")
open class CatalogueController(private val catalogueService: CatalogueService) {

    @Post
    open fun save(@Valid request: CreateProductCommand): Mono<HttpStatus> {
        return catalogueService.save(request)
            .map { added: Boolean -> if (added) HttpStatus.CREATED else HttpStatus.CONFLICT }
    }

    @Get("/{codeOrSku}")
    open fun findByCodeOrSku(codeOrSku: String): Mono<HttpResponse<ProductResponse>> {
        return catalogueService.find(codeOrSku).map { product: ProductResponse? ->
            if (product != null) HttpResponse.ok(product) else HttpResponse.notFound()
        }
    }
}