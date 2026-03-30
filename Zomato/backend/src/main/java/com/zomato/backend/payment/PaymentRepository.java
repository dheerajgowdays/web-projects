package com.zomato.backend.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);
}
