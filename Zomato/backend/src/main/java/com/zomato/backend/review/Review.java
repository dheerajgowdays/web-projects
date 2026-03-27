package com.zomato.backend.review;

import  com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.order.Order;
import com.zomato.backend.restaurant.Restaurant;
import com.zomato.backend.user.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="reviews")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Getter


public class Review extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="order_id",nullable=false,unique=true)
    private Order order;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="customer_id",nullable=false)
    private User user;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="restaurant_id",nullable=false)
    private Restaurant restaurantId;

    @Column(name="rating",nullable=false)
    private Short rating;

    @Column(name="comment",columnDefinition="TEXT")
    private String comment;

    @Column(name="food_rating")
    private Short foodRating;

    @Column(name="delivery_rating")
    private Short deliveryRating;

    @Column(name="packaging_rating")
    private Short packagingRating;

    @Column(name="image_urls",columnDefinition="jsonb")
    @Builder.Default
    private String imageUrls = "[]";

    @Column(name="owner_reply")
    private String ownerReplay;

    @Column(name="owner_replied_at")
    private LocalDateTime ownerRepliedAt;

    @Column(name="is_visible",nullable=false)
    @Builder.Default
    private Boolean isVisible = true;

}
