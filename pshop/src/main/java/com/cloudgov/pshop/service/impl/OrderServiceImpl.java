package com.cloudgov.pshop.service.impl;

import com.cloudgov.pshop.entity.Order;
import com.cloudgov.pshop.mapper.DTOMapper;
import com.cloudgov.pshop.repository.OrderRepository;
import com.cloudgov.pshop.repository.PizzaRepository;
import com.cloudgov.pshop.service.OrderService;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private PizzaRepository pizzaRepository;

    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Order> saveOrder(Order dto) {
        if (dto.getID() != null)
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id not allowed"));
        else {
            return validatePizzas( dto.getPizzas()  ).flatMap((flag) -> {
                        if (flag) {
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
        return orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj) -> {
            return validatePizzas( dto.getPizzas()).flatMap((flag) -> {
                        if (flag) {
                            return orderRepository.save(DTOMapper.map(obj, dto));
                        } else
                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Pizza Specified!"));
                    }
            );
        });
    }

    @Override
    public Mono<Void> deleteOrder(String id) {
        return orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj) -> {
            return orderRepository.deleteById(id);
        });
    }

    @Override
    public Mono<Order> softdeleteOrder(String id) {
        return orderRepository.findByIDAndStatusNot(id, "cancel").switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj) -> {
            obj.setStatus("cancel");
            return orderRepository.save(obj);
        });
    }

    @Override
    public Mono<Order> updateStatus(String status, String id) {
        return orderRepository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"))).flatMap((obj) -> {
            obj.setStatus(status);
            return orderRepository.save(obj);
        });
    }

    @Override
    public Flux<com.cloudgov.pshop.dto.Order> getOrderAggr(List<String> ids) {
        UnwindOperation unwindOperation = Aggregation.unwind("$pizzas");
        AddFieldsOperation addFieldsOperation = Aggregation.addFields().addFieldWithValueOf("pizzaObjectId", Map.of("$toObjectId", "$pizzas")).build();
        LookupOperation  lookupOperation = Aggregation.lookup("pizzas", "pizzaObjectId", "_id", "pizzas");
        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").in(ids));
        GroupOperation groupOperation = Aggregation.group(Fields.fields("ID", "$_id").and("status", "$status").and("timestamp", "$timestamp")).addToSet("$pizzas").as("pizzas");
        ProjectionOperation projectionOperation = Aggregation.project(Fields.fields("_id", "$_id.ID").and("status", "$_id.status").and("timestamp", "$_id.timestamp").and("pizzas"));
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "timestamp");
        TypedAggregation<Order> orderTypedAggregation = Aggregation.newAggregation(
                Order.class,
                matchOperation,
                unwindOperation,
                addFieldsOperation,
                lookupOperation,
                unwindOperation,
                groupOperation,
                projectionOperation,
                sortOperation
        );

        return reactiveMongoTemplate
                .aggregate(
                        orderTypedAggregation,
                        com.cloudgov.pshop.dto.Order.class
                );
    }

    Mono<Boolean> validatePizzas(List<String> ids) {
        if (ids.isEmpty()) return Mono.just(false);
        else
            return pizzaRepository.findAllById(ids).collectList().flatMap((objs) -> Mono.just(ids.size() == objs.size()));
    }
}
