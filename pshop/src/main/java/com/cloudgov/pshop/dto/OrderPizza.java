package com.cloudgov.pshop.dto;

import com.cloudgov.pshop.entity.Pizza;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPizza {
    String ID;
    List<Pizza> pizzas;
    String status;
    Long timestamp;
}
