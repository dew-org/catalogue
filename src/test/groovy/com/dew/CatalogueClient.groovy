package com.dew

import com.dew.catalogue.application.ProductResponse
import com.dew.catalogue.application.create.CreateProductCommand
import com.dew.catalogue.application.update.UpdateProductCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

import javax.validation.Valid

@Client("/catalogue")
interface CatalogueClient {

    @Post
    HttpStatus save(@Valid CreateProductCommand request)

    @Get("/{codeOrSku}")
    HttpResponse<ProductResponse> findByCodeOrSku(String codeOrSku)

    @Get
    List<ProductResponse> searchAll(@QueryValue("userId") String userId)

    @Put("/{code}")
    HttpResponse<UpdateProductCommand> update(String code, @Valid UpdateProductCommand request)
}