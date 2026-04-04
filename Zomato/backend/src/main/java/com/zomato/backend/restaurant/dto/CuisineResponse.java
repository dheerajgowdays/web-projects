package com.zomato.backend.restaurant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CuisineResponse {
    private Integer id;
    private String name;
    private String iconUrl;
}
