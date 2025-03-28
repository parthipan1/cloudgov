package com.cloudgov.pshop.service.impl;

import com.cloudgov.pshop.entity.Order;
import com.cloudgov.pshop.mapper.DTOMapper;
import com.cloudgov.pshop.repository.OrderRepository;
import com.cloudgov.pshop.repository.PizzaRepository;
import com.cloudgov.pshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private PizzaRepository pizzaRepository;

    @Override
    public Mono<Order> saveOrder(Order dto) {
        if(dto.getID()!=null) return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not allowed"));
        else {
            return validatePizzas(dto.getPizzas()).flatMap((flag) ->{
                            if(flag) {
                                return orderRepository.save(dto).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found")));
                            } else
                                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Pizza Specified!"));
                        }
                    );
        }
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
                return validatePizzas(dto.getPizzas()).flatMap((flag) ->{
                    if(flag) {
                        return orderRepository.save(DTOMapper.map(obj,dto));
                    } else
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Pizza Specified!"));
                    }
                    );
            });
    }

    @Override
    public Mono<Void> deleteOrder(String id) {
        return  orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
            return orderRepository.deleteById(id);
        });
    }

    @Override
    public Mono<Order> softdeleteOrder(String id) {
        return  orderRepository.findByIDAndStatusNot(id, "cancel").switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
           obj.setStatus("cancel");
           return orderRepository.save(obj);
        });
    }

    @Override
    public Mono<Order> updateStatus(String status, String id) {
        return  orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
            obj.setStatus(status);
            return orderRepository.save(obj);
        });
    }

    @Override
    public Mono<com.cloudgov.pshop.dto.Order> getOrderAggr(String id) {
        return  orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj)->{
            return pizzaRepository.findAllById(obj.getPizzas()).collectList().flatMap((objs) -> {
                return Mono.just(com.cloudgov.pshop.dto.Order.builder()
                                .ID(obj.getID())
                                .pizzas(objs)
                                .status(obj.getStatus())
                                .timestamp(obj.getTimestamp())
                        .build());
            });
        });
    }

    Mono<Boolean> validatePizzas(List<String> ids){
        if(ids.isEmpty()) return Mono.just(false);
        else return pizzaRepository.findAllById(ids).collectList().flatMap((objs) -> Mono.just(ids.size() == objs.size()));
    }
}
