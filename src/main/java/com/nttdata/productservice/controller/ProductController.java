package com.nttdata.productservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.productservice.entity.Product;
import com.nttdata.productservice.service.ProductService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/product")
public class ProductController {
	
	
	@Autowired
	ProductService productService;

	@GetMapping(/*produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE*/)
	public Flux<Product> findAll() {
		return productService.findAll();
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Product>> save(@RequestBody Product product) {
		return productService.save(product).map(_product -> ResponseEntity.ok().body(_product)).onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		});
	}


	@GetMapping(value="/{idProducto}")//,produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Product>> findById(@PathVariable(name = "idProducto") Long idProducto) {
		return productService.findById(idProducto).map(product -> ResponseEntity.ok().body(product))
				.onErrorResume(e -> {
					log.info(e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Product>> update(@RequestBody Product product) {
		// return productService.update(product);
		//// Verificar logica si aplica la busqueda del flatMap
		Mono<Product> mono = productService.findById(product.getIdProducto()).flatMap(objProduct -> {
			log.info("Update:[new]" + product + " [Old]:" + objProduct);
			return productService.update(product);
		});
		return mono.map(_product -> {
			log.info("Status:" + HttpStatus.OK);
			return ResponseEntity.ok().body(_product);
		}).onErrorResume(e -> {
			log.info("Status:" + HttpStatus.BAD_REQUEST + " menssage" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());

	};

	@DeleteMapping(value="/{idProducto}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> delete(@PathVariable(name = "idProducto") long idProducto) {
		return productService.findById(idProducto).flatMap(producto -> {
			log.info("Produc:"+producto.toString());
			productService.delete(producto.getIdProducto()).subscribe(log::info);
			return Mono.just(ResponseEntity.ok().build());
		});
	}

	@GetMapping(value="/fillData",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Void> fillData() {
		return productService.fillData();
	}

	
}
