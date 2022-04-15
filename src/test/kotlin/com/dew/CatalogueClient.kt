package com.dew

import com.dew.catalogue.application.ProductResponse
import com.dew.catalogue.application.create.CreateProductCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import javax.validation.Valid

@Client("/catalogue")
interface CatalogueClient {

    @Post
    fun save(@Valid request: CreateProductCommand): HttpStatus

    @Get("/{codeOrSku}")
    fun findByCodeOrSku(codeOrSku: String): HttpResponse<ProductResponse>
}