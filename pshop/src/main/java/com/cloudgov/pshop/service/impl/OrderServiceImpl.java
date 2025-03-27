package com.cloudgov.pshop.service.impl;

import com.cloudgov.pshop.entity.Order;
import com.cloudgov.pshop.entity.Pizza;
import com.cloudgov.pshop.mapper.DTOMapper;
import com.cloudgov.pshop.repository.OrderRepository;
import com.cloudgov.pshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Override
    public Mono<Order> saveOrder(Order dto) {
        return orderRepository.save(dto).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
    }

    @Override
    public Mono<Order> getOrder(String id) {
        return orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
    }

    @Override
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll().switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
    }

    @Override
    public Mono<Order> updateOrder(Order dto, String id) {
        return  orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
            return orderRepository.save(DTOMapper.map(obj,dto));
        });
    }

    @Override
    public Mono<Void> deleteOrder(String id) {
        return orderRepository.deleteById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
    }

    @Override
    public Mono<Order> updateStatus(String status, String id) {
        return  orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
            obj.setStatus(status);
            return orderRepository.save(obj);
        });
    }
}
