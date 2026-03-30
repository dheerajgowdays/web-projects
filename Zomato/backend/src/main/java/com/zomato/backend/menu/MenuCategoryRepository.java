package com.zomato.backend.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long>{

    List<MenuCategory> findByRestaurantIdAndIsActiveTrueOrderByDisplayOrderAsc
    (Long restaurantId);

    boolean existsByRestaurantIdAndNameIgnoreCase(Long restaurantId, String name);
}