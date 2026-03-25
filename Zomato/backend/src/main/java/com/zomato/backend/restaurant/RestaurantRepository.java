package com.zomato.backend.restaurant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    
    List<Repository> findByOwnerIdAndIsActiveTrue(Long ownerId);

    Page<Restaurant> findByCityAndIsActiveTrueAndIsApprovedTrue(
        String city, Pageable pageable
    );

    @Query("""
            SELECT r FROM Restaurant r
            WHERE r.Rcity = :city
            AND r.isActive = true
            AND r.isApproved = true
            AND LOWER(r.name) LIKE LOWER(CONCAT('%',:query,'%'))
            """)
    Page<Restaurant> searchByName(String city,String query,Pageable pageable);

    @Query(value = """
            SELECT * FROM restaurants r
        WHERE r.is_active = true
          AND r.is_approved = true
          AND (
            6371000 * acos(
              cos(radians(:lat)) * cos(radians(r.latitude)) *
              cos(radians(r.longitude) - radians(:lng)) +
              sin(radians(:lat)) * sin(radians(r.latitude))
            )
          ) <= :radiusMeters
        ORDER BY average_rating DESC
            """,nativeQuery = true)
    List<Restaurant> findNearby(double lat,double lng,double radiusMeters);

}
