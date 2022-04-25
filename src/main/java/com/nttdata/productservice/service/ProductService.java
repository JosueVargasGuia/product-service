package com.nttdata.productservice.service;

import com.nttdata.productservice.entity.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

	Flux<Product> findAll();
	Mono<Product> findById(Long idProducto);
	Mono<Product> save(Product product);
	Mono<Product> update(Product product);
	Mono<Void> delete(Long idProducto);
	Mono<Void> fillData();
	  Long generateKey(String nameTable);
}
