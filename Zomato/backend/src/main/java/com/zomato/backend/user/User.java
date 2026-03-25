package com.zomato.backend.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.common.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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

    @Column(name="is_email_verified",nullable=false)
    @Builder.Default
    private boolean  isEmailVerified = false;

    @Column(name="is_phone_verified",nullable=false)
    @Builder.Default
    private boolean isPhoneVerified = false;

    @Column(name= "is_active",nullable=false)
    @Builder.Default
    private boolean isActive = true;

    @Column(name="last_login_at")
    private LocalDateTime lastLoginAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(
            "ROLE_"+role.name()
        ));
    }

    @Override
    public String getPassword(){
        return passwordHash;
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){ return true;}

    @Override
    public boolean isAccountNonLocked(){ return isActive;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return isActive && isEmailVerified;}
}
