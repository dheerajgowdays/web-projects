package com.zomato.backend.payment;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zomato.backend.common.enums.PaymentMethod;
import com.zomato.backend.common.enums.PaymentStatus;
import com.zomato.backend.order.Order;

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

@Entity
@Table(name="payments")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id",nullable=false,unique=true)
    private Order orderId;

    @Column(name="amount_paise",nullable=false)
    private Integer amountPaise;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_method",nullable=false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name="gateway_order_id",length=100)
    private String gatewayOrderId;

    @Column(name="gateway_payment_id",length=100)
    private String gatewayPaymentId;

    @Column(name="gateway_signature",length=255)
    private String  gatewaySignature;

    @Column(name="gateway_response",columnDefinition="jsonb")
    private String gatewayResponse;

    @Column(name="refund_id",length=100)
    private String refundId;

    @Column(name="refund_amount_paise")
    @Builder.Default
    private Integer refundAmountPaise = 0;

    @Column(name="refund_at")
    private LocalDateTime refundAt;

    @Column(name="refund_reason")
    private String refundReason;

    @Column(name="initiated_at",nullable=false)
    @Builder.Default
    private LocalDateTime initiatedAt = LocalDateTime.now();

    @Column(name="paid_at")
    private LocalDateTime paidAt;

    @Column(name="failed_at")
    private LocalDateTime failedAt;

    @CreationTimestamp
    @Column(name="created_at",updatable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

}
