// AddressRepository.java
package com.zomato.backend.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // All saved addresses for a user (for address selection at checkout)
    List<Address> findByUserIdOrderByIsDefaultDescCreatedAtDesc(Long userId);

    // Get the user's default address
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);

    // Before setting a new default, clear the current one
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void clearDefaultForUser(Long userId);
}