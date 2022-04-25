package com.nttdata.productservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.productservice.entity.Product;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product,Long>{
}
