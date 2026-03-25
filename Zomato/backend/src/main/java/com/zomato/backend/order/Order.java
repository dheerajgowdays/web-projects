package com.zomato.backend.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.zomato.backend.common.enums.OrderStatus;
import com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.restaurant.Restaurant;
import com.zomato.backend.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Order extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_number",nullable=false,unique = true,length=20)
    private String orderNumber;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="customer_id",nullable=false)
    private User customer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="restaurant_id",nullable=false)
    private Restaurant restaurant;

    @Column(name="delivery_address_line1",nullable=false)
    private String deliveryAddressLine1;

    @Column(name= "delivery_address_line2")
    private String deliveryAddressLine2;

    @Column(name="delivery_landmark")
    private String deliveryLandmark;

    @Column(name="delivery_city",nullable=false,length=100)
    private String deliveryCity;

    @Column(name="delivery_state",nullable=false,length=100)
    private String deliveryState;

    @Column(name="delivery_pincode",nullable=false,length=10)
    private String deliveryPincode;

    @Column(name= "subtotal_paise",nullable=false)
    private Integer subtotalPaise;

    @Column(name="delivery_fee_paise",nullable=false)
    @Builder.Default
    private Integer deliveryFeePaise = 0;

    @Column(name="tax_paise",nullable=false)
    @Builder.Default
    private Integer taxPaise=0;

    @Column(name="discount_paise",nullable=false)
    @Builder.Default
    private Integer discountPaise = 0;

    @Column(name="coupon_code",length=50)
    private String couponCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable= false,length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name ="special_instructions",columnDefinition= "TEXT")
    private String specialInstructions;

    @Column(name = "placed_at",nullable = false)
    @Builder.Default
    private LocalDateTime placed_at = LocalDateTime.now();

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "preparing_at")
    private LocalDateTime preparingAt;

    @Column(name = "ready_at")
    private LocalDateTime readyAt;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name ="cancelled_at")
    private LocalDateTime cancelldAt;

    @Column(name = "cancellation_reason",columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name= "cancelled_by",length=20)
    private String cancelledBy;

    @Column(name="estimated_delivery_minutes")
    private Integer estimatedDeliveryMinutes;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();


}
