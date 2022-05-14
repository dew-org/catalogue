package com.dew.catalogue.domain

import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import javax.validation.Valid

interface CatalogueRepository {

    fun save(@Valid product: Product): Mono<Boolean>

    fun find(codeOrSku: String): Mono<Product>

    fun searchAll(): Publisher<Product>

    fun update(@Valid product: Product): Mono<Boolean>
}