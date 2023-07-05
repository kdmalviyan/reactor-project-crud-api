package com.reactor.productapi.products;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author kuldeep
 */
@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Flux<Product> findAllProducts() {
        return productService.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> findProduct(@PathVariable("id") String id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Product>> createProduct(@RequestBody Product product) {
        return productService
                .create(product).map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping
    public Mono<ResponseEntity<Product>> updateProduct(@RequestBody Product product) {
        return productService
                .update(product).map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

}
