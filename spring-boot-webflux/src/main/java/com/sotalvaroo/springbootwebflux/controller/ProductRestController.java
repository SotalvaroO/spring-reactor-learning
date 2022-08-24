package com.sotalvaroo.springbootwebflux.controller;

import com.sotalvaroo.springbootwebflux.config.DbSeederConfig;
import com.sotalvaroo.springbootwebflux.model.document.Product;
import com.sotalvaroo.springbootwebflux.model.repository.IProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/products")
public class ProductRestController {

    private final Logger log = LoggerFactory.getLogger(DbSeederConfig.class);

    @Autowired
    private IProductRepository productRepository;

    @GetMapping
    public Flux<Product> index(){
        Flux<Product> products = productRepository.findAll()
                .map(i -> {
                    i.setName(i.getName().toUpperCase());
                    return i;
                }).doOnNext(prod ->log.info(prod.getName()));

        return products;
    }

    @GetMapping(value = "/{id}")
    public Mono<Product> show(@PathVariable("id") String id){
        Mono<Product> productMono = productRepository.findById(id);
        return  productMono;
    }

}
