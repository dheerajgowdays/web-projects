package com.zomato.backend.menu;

import com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.common.enums.FoodType;
import com.zomato.backend.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="menu_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MenuItem extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="restaurant_id",nullable=false)
    private Restaurant restaurant;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category_id",nullable=false)
    private MenuCategory category;

    @Column(nullable=false,length = 255)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(name="image_url",columnDefinition= "TEXT")
    private String imageUrl;

    @Column(name="price_paise",nullable=false)
    private Integer pricePaise;

    @Column(name="original_price_paise")
    private Integer originalPriceaPaise;

    @Enumerated(EnumType.STRING)
    @Column(name="food_type",nullable=false,length=10)
    @Builder.Default
    private FoodType foodType = FoodType.VEG;

    @Column(name = "is_available",nullable=false)
    @Builder.Default
    private boolean isAvailable = true;

    @Column(name= "is_bestseller",nullable=false)
    @Builder.Default
    private boolean isBestseller = false;

    @Column(name="spice_level")
    @Builder.Default
    private Short spiceLevel = 0;

    @Column(name="prepration_time_minutes")
    private Short preparationTimeMinutes;

    private Short calories;

    @Column(name="average_rating",nullable = false,precision=3,scale = 2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name="total_ratings",nullable = false)
    @Builder.Default
    private Integer totalRatings = 0;

}
