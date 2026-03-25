package com.zomato.backend.order;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zomato.backend.common.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Customer's order history — paginated, newest first
    Page<Order> findByCustomerIdOrderByPlacedAtDesc(Long customerId, Pageable pageable);

    // Restaurant's incoming orders — filtered by status
    Page<Order> findByRestaurantIdAndStatusOrderByPlacedAtDesc(
        Long restaurantId, OrderStatus status, Pageable pageable
    );

    // Get order with all items in a single query (prevents N+1)
    // JOIN FETCH tells Hibernate to load items in the same SQL query
    // instead of making a separate query per order (N+1 problem)
    @Query("""
        SELECT o FROM Order o
        JOIN FETCH o.items
        WHERE o.id = :id
        """)
    Optional<Order> findByIdWithItems(Long id);

    // Order tracking — customer polling for status
    Optional<Order> findByOrderNumberAndCustomerId(String orderNumber, Long customerId);

    boolean existsByOrderNumber(String orderNumber);
}