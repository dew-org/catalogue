package com.dew.catalogue.domain

import reactor.core.publisher.Mono
import javax.validation.Valid

interface CatalogueRepository {

    fun save(@Valid product: Product): Mono<Boolean>
}