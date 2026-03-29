package com.zomato.backend.cart;

import com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.menu.MenuItem;
import com.zomato.backend.user.User;
import com.zomato.backend.restaurant.Restaurant;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items") 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CartItems extends BaseEntity{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id" , nullable=false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="restaurant_id",nullable=false)
    private Restaurant restaurantID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="menu_item_id",nullable=false)
    private MenuItem menuItem;

    @Column(name="quantity",nullable=false)
    @Builder.Default
    private Short quantity=1;

    @Column(name="customization_note")
    private String customizationNote;
}

