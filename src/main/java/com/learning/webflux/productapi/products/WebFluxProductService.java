package com.learning.webflux.productapi.products;

import com.learning.reactor.productapi.products.Product;
import com.learning.reactor.productapi.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author kuldeep
 */
@Service
@RequiredArgsConstructor
public class WebFluxProductService {
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

    public Mono<Product> update(Mono<Product> productMono, String id) {
        Mono<Product> existingProductMono = productRepository.findById(id);
        Mono<Product> updatedProduct = productMono.zipWith(existingProductMono, (product, existingProduct)
                -> new Product(existingProduct.getId(), product.getName(), product.getPrice()));
        return updatedProduct.flatMap(p -> productRepository.save(p));
    }

    public Mono<Void> delete(String id) {
        return productRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return productRepository.deleteAll();
    }
}
