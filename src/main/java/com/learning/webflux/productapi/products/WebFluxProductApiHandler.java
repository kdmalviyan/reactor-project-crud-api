package com.learning.webflux.productapi.products;

import com.learning.reactor.productapi.products.Product;
import com.learning.reactor.productapi.products.ProductEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.beans.EventHandler;
import java.time.Duration;

/**
 * @author kuldeep
 */
@Component
@RequiredArgsConstructor
public class WebFluxProductApiHandler {
    private final WebFluxProductService productService;

    public Mono<ServerResponse> getALlProducts(ServerRequest serverRequest) {
        Flux<Product> products = productService.findAll();
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products, Product.class);
    }

    public Mono<ServerResponse> getProductById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Product> product = productService.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return product.flatMap(p -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(p)))
                .switchIfEmpty(notFound);

//        return ServerResponse
//                .ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(product, Product.class);
    }

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        Mono<Product> product = serverRequest.bodyToMono(Product.class);
        return product.flatMap(p -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.create(p), Product.class));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Product> product = serverRequest.bodyToMono(Product.class);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.update(product, id), Product.class)
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .ok()
                .build(productService.delete(id))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteAll(ServerRequest serverRequest) {
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return ServerResponse
                .ok()
                .build(productService.deleteAll())
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getEvents(ServerRequest serverRequest) {
        Flux<ProductEvent> productEvents = Flux.interval(Duration.ofSeconds(1))
                .map(e -> new ProductEvent(e.toString(), "product Event"));
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productEvents, EventHandler.class);
    }
}
