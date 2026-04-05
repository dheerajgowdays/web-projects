package com.zomato.backend.restaurant.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantSummaryResponse {
    private Long id;
    private String name;
    private String coverImageUrl;
    private String logoUrl;
    private BigDecimal averageRating;
    private Integer totalRatings;
    private Short deliveryTimeMin;
    private Integer deliveryTimeMax;
}
