package com.zomato.backend.delivery;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.zomato.backend.delivery.DeliveryPartner;
import com.zomato.backend.order.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="delivery_assignments")
@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAssignments{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id",nullable=false,unique=true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="delivery_partner_id",nullable=false)
    private DeliveryPartner deliveryPartnerId;

    @Column(name="assigned_at",nullable=false)
    @Builder.Default
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name="accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name="rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name="picked_up_at")
    private LocalDateTime pickedUpAt;

    @Column(name="delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name="delivery_latitude",precision=10,scale=8)
    private BigDecimal deliveryLatitude;

    @Column(name="delivery_longitude",precision=11,scale=8)
    private BigDecimal deliveryLongitude;

    @Column(name="partner_rating")
    private Short partnerRating;

    @Column(name="distance_meters")
    private Integer distanceMeters;

    @Column(name="earnings_paise")
    @Builder.Default
    private Integer earningsPaise=0;
}