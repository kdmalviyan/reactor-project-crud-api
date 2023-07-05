package com.reactor.productapi.products;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author kuldeep
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Mono<Product> create(Product product) {
        return productRepository.save(product);
    }

    public Mono<Product> update(Product product) {
        return productRepository.save(product);
    }
}
