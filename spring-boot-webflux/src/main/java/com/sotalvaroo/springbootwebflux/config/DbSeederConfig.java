package com.sotalvaroo.springbootwebflux.config;

import com.sotalvaroo.springbootwebflux.model.document.Product;
import com.sotalvaroo.springbootwebflux.model.repository.IProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Date;

@Component
/*@EnableAutoConfiguration*/
public class DbSeederConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final IProductRepository productRepository;

    /*private final ReactiveMongoTemplate mongoTemplate;*/

    private final Logger log = LoggerFactory.getLogger(DbSeederConfig.class);

    public DbSeederConfig(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        deleteProducts();
        createProducts();

    }

    private void deleteProducts() {

//        mongoTemplate.dropCollection("products").subscribe();             Opcion 1
        productRepository.deleteAll().subscribe();

    }

    private void createProducts() {
        Flux.just(new Product("Play Station 4", 3500000.0)
                        , new Product("XBox One", 3700000.0)
                        , new Product("Nintendo Switch", 1650000.0)
                        , new Product("Guitarra del Gitar hero", 450000.0)
                        //no se puede usar map porque devuelve un Mono<Product> necesitamos flatmap
                )
                .flatMap(product -> {
                    product.setCreateAt(new Date());
                    return productRepository.save(product);
                })
                .subscribe(i -> log.info("Insert" + i.getId() + " " + i.getName()));

    }

}
