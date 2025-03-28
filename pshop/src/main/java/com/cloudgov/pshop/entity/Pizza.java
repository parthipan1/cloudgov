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
@Document(value = "pizzas")
public class Pizza {
    @Id
    String ID;
    @NotBlank(message = "name is mandatory")
    String name;
    String description;
    @NotNull(message = "toppings is mandatory")
    List<String> toppings;
    @NotBlank(message = "size is mandatory")
    String size;
    @NotNull(message = "price is mandatory")
    Double price;

}
