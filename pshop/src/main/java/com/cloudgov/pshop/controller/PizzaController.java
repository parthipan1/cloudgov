package com.cloudgov.pshop.controller;

import com.cloudgov.pshop.entity.Pizza;
import com.cloudgov.pshop.service.PizzaService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/pizzas")
@AllArgsConstructor
public class PizzaController {

    private PizzaService pizzaService;


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Pizza> savePizza(@NotNull @Validated @RequestBody Pizza dto){
        return pizzaService.savePizza(dto);
    }

    @GetMapping("{id}")
    public Mono<Pizza> getPizza(@NotNull @PathVariable("id") String id){
        return pizzaService.getPizza(id);
    }

    @GetMapping
    public Flux<Pizza> getAllPizzas(){
        return pizzaService.getAllPizzas();
    }

    @PutMapping("{id}")
    public Mono<Pizza> updatePizza(@NotNull @Validated @RequestBody Pizza dto,
                                   @NotNull @PathVariable("id") String id){
        return pizzaService.updatePizza(dto, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> deletePizza(@NotNull @PathVariable("id") String id){
        return pizzaService.deletePizza(id);
    }

}
