package com.zomato.backend.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItems, Long>{
    
    @Query("""
            SELECT ci FROM CartItem ci
            JOIN FETCH ci.menuitem
            JOIN FETCH ci.restaurant
            WHERE ci.user.id = :userId
            """)
    List<CartItems> findByUserIdWithDetails(Long userId);

    Optional<CartItems> findByUserIdAndMenuItemId(Long userId, Long menuitemId);

    @Modifying
    @Query("DELETE FROM CartItems ci WHERE ci.user.id = :userId")
    void deleteAllByUserId(Long userId);
}
