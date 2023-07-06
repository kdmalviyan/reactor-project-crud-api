package com.learning.reactor.productapi.products;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author kuldeep
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
