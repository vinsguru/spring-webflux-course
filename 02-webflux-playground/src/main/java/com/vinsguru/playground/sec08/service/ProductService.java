package com.vinsguru.playground.sec08.service;

import com.vinsguru.playground.sec08.dto.ProductDto;
import com.vinsguru.playground.sec08.mapper.EntityDtoMapper;
import com.vinsguru.playground.sec08.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Flux<ProductDto> saveProducts(Flux<ProductDto> flux) {
        return flux.map(EntityDtoMapper::toEntity)
                   .as(this.repository::saveAll)
                   .map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductsCount() {
        return this.repository.count();
    }

    public Flux<ProductDto> allProducts() {
        return this.repository.findAll()
                              .map(EntityDtoMapper::toDto);
    }

}
