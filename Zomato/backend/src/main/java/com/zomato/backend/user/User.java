package com.zomato.backend.user;

import org.springframework.security.core.userdetails.UserDetails;

import com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.common.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Cleanup;

public class User extends BaseEntity implements  UserDetails{
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name",nullable=false,length=100)
    private String fullName;

    @Column(nullable=false,unique=true,length=255)
    private String email;

    @Column(name="password_hash",length=255)
    private String passwordHash;

    @Column(length=15,unique=true)
    private String phone;

    @Column(name="profile_picture_url")
    private String  profilePictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    @Builder.Default
    private UserRole role = UserRole.CUSTOMER;

    
}
