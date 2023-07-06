package com.learning;

import com.learning.reactor.productapi.products.Product;
import com.learning.reactor.productapi.products.ProductRepository;
import com.learning.webflux.productapi.products.WebFluxProductApiHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.http.MediaType;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;


@SpringBootApplication
public class ProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ReactiveMongoOperations reactiveMongoOperations,
						   ProductRepository productRepository) {
		return args -> {
			Flux<Product> products = Flux.just(
					new Product(null, "Big Latte", 2.99),
					new Product(null, "Big Decaf", 4.99),
					new Product(null, "Green Tea", 1.99)
			).flatMap(productRepository::save);
			products.thenMany(productRepository.findAll()).subscribe(System.out::println);

//			reactiveMongoOperations.collectionExists(Product.class)
//					.flatMap(exists -> exists ?
//							reactiveMongoOperations.dropCollection(Product.class)
//							: Mono.just(false))
//					.thenMany(v -> reactiveMongoOperations.createCollection(Product.class))
//					.thenMany(products)
//					.thenMany(productRepository.findAll())
//					.subscribe(System.out::println);
		};
	}

	@Bean
	public RouterFunction<ServerResponse> productRoutes(WebFluxProductApiHandler handler) {

		return route()
				.path("webflux//products",
						builder -> builder
						.nest(accept(MediaType.APPLICATION_JSON)
										.or(contentType(MediaType.APPLICATION_JSON)
												.or(accept(MediaType.TEXT_EVENT_STREAM))),
								nestedBuilder -> nestedBuilder
										.GET("/events", handler::getEvents)
										.GET("/{id}", handler::getProductById)
										.GET(handler::getALlProducts)
										.PUT("/{id}", handler::updateProduct)
										.POST(handler::createProduct))
				)
				.DELETE("/{id}", handler::delete)
				.DELETE(handler::deleteAll)
				.build();


//		return route()
//				.GET("/products/events", accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)), handler::getEvents)
//				.GET("/products/{id}}", accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)), handler::getProductById)
//				.GET("/products", accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)), handler::getALlProducts)
//
//
//				.POST("/products", accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)), handler::createProduct)
//
//				.PUT("/products/{id}", accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)), handler::updateProduct)
//
//				.DELETE("/products/{id}", accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)), handler::delete)
//				.DELETE("/products", accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE)), handler::deleteAll)
//				.build();
	}
}
