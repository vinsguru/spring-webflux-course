package com.vinsguru.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ProductDto {

    private String id;
    private String description;
    private Integer price;

    public ProductDto(String description, Integer price) {
        this.description = description;
        this.price = price;
    }
}
