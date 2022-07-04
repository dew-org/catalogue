package com.dew

import com.dew.catalogue.application.CatalogueService
import com.dew.catalogue.application.ProductResponse
import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.catalogue.application.update.UpdateProductCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.annotation.RequestAttribute
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller("/catalogue")
@Secured(SecurityRule.IS_AUTHENTICATED)
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

    @Get
    open fun searchAll(@QueryValue("userId") userId: String): Publisher<ProductResponse> {
        return catalogueService.searchAll(userId)
    }

    @Put("/{code}")
    open fun update(code: String, @Valid request: UpdateProductCommand): Mono<HttpResponse<UpdateProductCommand>> {
        return catalogueService.update(code, request)
            .map { updated: Boolean -> if (updated) HttpResponse.ok(request) else HttpResponse.notFound() }
    }
}