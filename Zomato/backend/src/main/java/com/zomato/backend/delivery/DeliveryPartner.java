package com.zomato.backend.delivery;

import java.math.BigDecimal;

import com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.common.enums.DeliveryPartnerStatus;
import com.zomato.backend.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name="delivery_partners")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DeliveryPartner extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",unique=true,nullable=false)
    private User userId;

    @Column(name="vehicle_type",nullable=false,length=50)
    private String vehicleType;

    @Column(name="vehicle_number",length=20)
    private String vehicleNumber;

    @Column(name="license_number",length=50)
    private String licenseNumber;

    @Column(name="aadhar_number",length=12)
    private String aadharNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Builder.Default
    private DeliveryPartnerStatus status = DeliveryPartnerStatus.OFFLINE;

    @Column(name="current_latitude",precision=10,scale=8)
    private BigDecimal currentLatitude;
    
    @Column(name="current_longitude",precision=11,scale=8)
    private BigDecimal currentLongitude;

    @Column(name="location_updated_at")
    private LocalDateTime locationUpdatedTime;

    @Column(name="total_deliveries",nullable=false)
    @Builder.Default
    private Integer totalDeliveries = 0;

    @Column(name="average_rating",nullable=false,precision=3,scale=2)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name="city",length=100)
    private String city;

    @Column(name="is_verified",nullable=false)
    @Builder.Default
    private boolean idVerified = false;

    @Column(name="is_active",nullable=false)
    @Builder.Default
    private boolean isActive = false;
}
