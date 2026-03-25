package com.zomato.backend.restaurant;

import java.math.BigDecimal;

import  com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Restaurant extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id",nullable=false)
    private User owner;

    @Column(nullable=false,length = 255)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(nullable=false,length = 15)
    private String phone;

    @Column(length=255)
    private String email;

    @Column(name= "address_line1",nullable=false)
    private String addressLine1;

    @Column(name= "address_line2")
    private String addressLine2;

    private String landmark;

    @Column(nullable=false,length = 100)
    private String city;

    @Column (nullable=false,length = 100)
    private String state;

    @Column (nullable=false,length=10)
    private String pincode;

    @Column(nullable=false,precision=10,scale=8)
    private BigDecimal latitude;

    @Column(nullable=false,precision=11,scale=8)
    private BigDecimal longitude;

    @Column (name="cover_image_url",columnDefinition="TEXT")
    private String coverImageUrl;

    @Column (name="logo_url",columnDefinition="TEXT")
    private String logoUrl;

    @Column (name="price_range")
    private Short   priceRange;

    @Column (name="min_order_paise",nullable = false)
    @Builder.Default
    private Integer minOrderPaise = 0;

    @Column(name="delivery_time_min",nullable=false)
    @Builder.Default
    private Integer deliveryTimeMin = 30;

    @Column(name="delivery_fee_paise",nullable=false)
    @Builder.Default
    private Integer deliveryFeePaise = 0;

    @Column(name="delivery_time_max",nullable=false)
    @Builder.Default
    private Short deliveryTimeMax = 45;

    @Column(name="average_rating",nullable=false,precision=3,scale=2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name="total_ratings",nullable=false)
    @Builder.Default
    private Integer totalRatings = 0;

    @Column(name="is_open",nullable=false)
    @Builder.Default
    private boolean isOpen = false;

    @Column(name="is_approved",nullable=false)
    @Builder.Default
    private boolean isApproved =false;

    @Column(name="is_active",nullable=false)
    @Builder.Default
    private boolean isActive = true;

    @Column(name="fssai_license",length=20)
    private String fssaiLicense;
}
