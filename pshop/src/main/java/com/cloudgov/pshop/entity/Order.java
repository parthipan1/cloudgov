package com.cloudgov.pshop.entity;

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
    @NotBlank(message = "pizzas is mandatory")
    List<String> pizzas;
    @NotBlank(message = "status is mandatory")
    String status;
    @NotBlank(message = "timestamp is mandatory")
    long timestamp;
}
