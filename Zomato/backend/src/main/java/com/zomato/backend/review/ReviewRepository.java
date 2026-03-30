package com.zomato.backend.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import java.util.Optional;

public interface  ReviewRepository extends JpaRepository<Review, Long>{
    
    Page<Review> findByRestaurantIdAndIsVisibleTrueOrderByCreatedAtDesc(Long restaurantId,Pageable pageable);

    Optional<Review> findByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);
}
