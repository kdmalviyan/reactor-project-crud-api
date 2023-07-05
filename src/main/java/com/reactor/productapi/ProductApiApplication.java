package com.reactor.productapi;

import com.reactor.productapi.products.Product;
import com.reactor.productapi.products.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
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
}
