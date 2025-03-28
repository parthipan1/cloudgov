package com.cloudgov.pshop.controller;

import com.cloudgov.pshop.entity.Order;
import com.cloudgov.pshop.service.OrderService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<Order> saveOrder(@NotNull @Validated @RequestBody Order dto){
        return orderService.saveOrder(dto);
    }

    @GetMapping("{id}")
    public Mono<Order> getOrder(@NotNull @PathVariable("id") String id){
        return orderService.getOrder(id);
    }

    @GetMapping
    public Flux<Order> getAllOrders(){
        return orderService.getAllOrders();
    }
    
    @PatchMapping("{id}/{status}")
    public Mono<Order> updateStatus(@NotNull @PathVariable("status") String status,
                                   @NotNull  @PathVariable("id") String id){
        return orderService.updateStatus(status, id);
    }

    @PutMapping("{id}")
    public Mono<Order> updateOrder(@NotNull @Validated @RequestBody Order dto,
                                   @NotNull  @PathVariable("id") String id){
        return orderService.updateOrder(dto, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteOrder(@NotNull @PathVariable("id") String id){
       return  orderService.updateStatus("cancel", id).flatMap((obj)->Mono.empty());
    }

}
