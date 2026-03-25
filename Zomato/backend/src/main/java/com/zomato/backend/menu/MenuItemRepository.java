package com.zomato.backend.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategoryIdAndIsAvailableTrueOrderByDisplayOrderAsc(Long categoryId);

    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);

    // Find bestsellers for a restaurant
    List<MenuItem> findByRestaurantIdAndIsBestsellerTrueAndIsAvailableTrue(Long restaurantId);
}