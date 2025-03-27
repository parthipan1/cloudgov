package com.cloudgov.pshop.repository;


import com.cloudgov.pshop.entity.Pizza;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository  extends ReactiveCrudRepository<Pizza, String> {
}
