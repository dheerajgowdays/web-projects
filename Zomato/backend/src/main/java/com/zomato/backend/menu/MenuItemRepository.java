package com.zomato.backend.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface MenuItemRepository extends JpaRepository<MenuItem, Long>{

    List<MenuItem> findByCategoryIdAndAvailableTrueOrderByDisplayOrderAsc(Long categoryId);
    List<MenuItem> findByRestaurantId(Long restaurantId);
    boolean existsByIdAndRestaurantId(Long id, Long restaurantId);
}