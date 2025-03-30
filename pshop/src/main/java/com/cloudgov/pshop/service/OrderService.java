package com.cloudgov.pshop.service;

import com.cloudgov.pshop.entity.Order;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface OrderService {

    Mono<Order> saveOrder(Order dto);

    Mono<Order> getOrder(String  id);

    Flux<Order> getAllOrders();

    Mono<Order> updateOrder(Order dto, String id);

    Mono<Void> deleteOrder(String id);

    Mono<Order> softdeleteOrder(String id);

    Mono<Order> updateStatus( String status, String id);

   default  Mono<com.cloudgov.pshop.dto.Order> getOrderAggr(@NotNull String id){
       return getOrderAggr(Collections.singletonList(id)).collectList().flatMap((list) -> {
           if (!list.isEmpty())
               return Mono.just(list.getFirst());
           else
               return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"));
       });
   }

    Flux<com.cloudgov.pshop.dto.Order> getOrderAggr(List<String> ids);
}
