package com.cloudgov.pshop.service.impl;

import com.cloudgov.pshop.entity.Pizza;
import com.cloudgov.pshop.mapper.DTOMapper;
import com.cloudgov.pshop.repository.PizzaRepository;
import com.cloudgov.pshop.service.PizzaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PizzaServiceImpl implements PizzaService {

    private PizzaRepository pizzaRepository;

    @Override
    public Mono<Pizza> savePizza(Pizza dto) {
        if(dto.getID()!=null) return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not allowed"));
        else return pizzaRepository.save(dto).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
    }

    @Override
    public Mono<Pizza> getPizza(String id) {
        return pizzaRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
    }

    @Override
    public Flux<Pizza> getAllPizzas() {
        return pizzaRepository.findAll().switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
    }

    @Override
    public Mono<Pizza> updatePizza(Pizza dto, String id) {
        return  pizzaRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
            return pizzaRepository.save(DTOMapper.map(obj,dto));
        });
    }

    @Override
    public Mono<Void> deletePizza(String id) {
        return  pizzaRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
            return pizzaRepository.deleteById(id);
        });
    }
}
