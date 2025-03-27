package com.cloudgov.pshop.service;

import com.cloudgov.pshop.entity.Pizza;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PizzaService {

    Mono<Pizza> savePizza(Pizza dto);

    Mono<Pizza> getPizza(String  id);

    Flux<Pizza> getAllPizzas();

    Mono<Pizza> updatePizza(Pizza dto, String id);

    Mono<Void> deletePizza(String id);

}
