package com.cloudgov.pshop.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "orders")
public class Order {
    @Id
    String ID;
    @NotNull(message = "pizzas is mandatory")
    List<String> pizzas;
    @NotBlank(message = "status is mandatory")
    String status;
    @NotNull(message = "timestamp is mandatory")
    Long timestamp;
}
