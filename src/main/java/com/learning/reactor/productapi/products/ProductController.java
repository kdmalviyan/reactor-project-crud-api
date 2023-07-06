package com.learning.reactor.productapi.products;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

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

    @PutMapping("{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@RequestBody Product product, @PathVariable("id") String id) {
        return productService
                .update(product, id).map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return productService
                .delete(id).map(p -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<ResponseEntity<Void>> deleteAll() {
        return productService
                .deleteAll().map(p -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(e -> new ProductEvent(e.toString(), "product Event"));
    }

}
