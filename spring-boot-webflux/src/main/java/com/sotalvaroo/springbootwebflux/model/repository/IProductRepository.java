package com.sotalvaroo.springbootwebflux.model.repository;

import com.sotalvaroo.springbootwebflux.model.document.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface IProductRepository extends ReactiveMongoRepository<Product, String> {
}
