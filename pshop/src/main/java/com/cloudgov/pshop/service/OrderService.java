package com.cloudgov.pshop.service;

import com.cloudgov.pshop.entity.Order;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<Order> saveOrder(Order dto);

    Mono<Order> getOrder(String  id);

    Flux<Order> getAllOrders();

    Mono<Order> updateOrder(Order dto, String id);

    Mono<Void> deleteOrder(String id);

    Mono<Order> softdeleteOrder(String id);
    
    Mono<Order> updateStatus( String status, String id);
}
