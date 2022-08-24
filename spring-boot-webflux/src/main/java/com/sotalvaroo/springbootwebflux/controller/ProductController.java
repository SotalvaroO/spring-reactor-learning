package com.sotalvaroo.springbootwebflux.controller;

import com.sotalvaroo.springbootwebflux.config.DbSeederConfig;
import com.sotalvaroo.springbootwebflux.model.document.Product;
import com.sotalvaroo.springbootwebflux.model.repository.IProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;


@Controller
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(DbSeederConfig.class);

    @Autowired
    private IProductRepository productRepository;

    @GetMapping({"/products","/"})
    public String listProducts(Model model){
        Flux<Product> products = productRepository.findAll()
                .map(i -> {
                    i.setName(i.getName().toUpperCase());
                    return i;
                });

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products",products);
        model.addAttribute("title","Products list");
        return "list-products";
    }

    @GetMapping("/products-data-driver")
    public String listProductsDataDriver(Model model){
        Flux<Product> products = productRepository.findAll()
                .map(i -> {
                    i.setName(i.getName().toUpperCase());
                    return i;
                })
                .delayElements(Duration.ofSeconds(1));

        products.subscribe(product -> log.info(product.getName()));

        model.addAttribute("products",new ReactiveDataDriverContextVariable(products,2));
        model.addAttribute("title","Products list");
        return "list-products";
    }

    @GetMapping("/products-full")
    public String listProductsFull(Model model){
        Flux<Product> products = productRepository.findAll()
                .map(i -> {
                    i.setName(i.getName().toUpperCase());
                    return i;
                })
                .repeat(5000);

        model.addAttribute("products",new ReactiveDataDriverContextVariable(products,2));
        model.addAttribute("title","Products list");
        return "list-products";
    }

    @GetMapping("/products-chunked")
    public String listProductsChunked(Model model){
        Flux<Product> products = productRepository.findAll()
                .map(i -> {
                    i.setName(i.getName().toUpperCase());
                    return i;
                })
                .repeat(5000);

        model.addAttribute("products",new ReactiveDataDriverContextVariable(products,2));
        model.addAttribute("title","Products list");
        return "list-products-chunked";
    }

}
