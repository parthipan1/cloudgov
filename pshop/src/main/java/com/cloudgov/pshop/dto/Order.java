package com.cloudgov.pshop.dto;

import com.cloudgov.pshop.entity.Pizza;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    String ID;
    @NotNull(message = "pizzas is mandatory")
    List<Pizza> pizzas;
    @NotBlank(message = "status is mandatory")
    String status;
    @NotNull(message = "timestamp is mandatory")
    Long timestamp;
}