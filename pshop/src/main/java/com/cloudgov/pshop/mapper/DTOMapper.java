package com.cloudgov.pshop.mapper;

import com.cloudgov.pshop.entity.Order;
import com.cloudgov.pshop.entity.Pizza;

public class DTOMapper {

    public static Order map(Order existing, Order dto){
        existing.setStatus(dto.getStatus());
        existing.setTimestamp(dto.getTimestamp());
        existing.setPizzas(dto.getPizzas());
        return existing;
    }


    public static Pizza map(Pizza existing, Pizza dto){
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setToppings(dto.getToppings());
        existing.setSize(dto.getSize());
        existing.setPrice(dto.getPrice());
        return existing;
    }
}
