package com.zomato.backend.restaurant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantSummaryResponse {
    private Long id;
    private String name;
    private String coverImageUrl;
}
