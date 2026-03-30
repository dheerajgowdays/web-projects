package com.zomato.backend.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import com.zomato.backend.common.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

    Page<Order> findByCustomerIdOrderByPlacedAtDesc(Long customerId, Pageable pageable);

    Page<Order> findByRestaurantIdAndStatusInOrderByPlacedAtDesc(Long restaurantId, List<OrderStatus> statuses,Pageable pageable);

    List<Order> findByCustomerIdAndStatusNotIn(Long customerId,List<OrderStatus> statuses);

    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.items
            WHERE o.id = :id
            """)
    Optional<Order> findByIdWithItems(Long id); 
}