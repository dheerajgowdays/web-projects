package com.zomato.backend.menu;

import com.zomato.backend.common.entity.BaseEntity;
import com.zomato.backend.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="menu_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MenuCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="restaurant_id",nullable=false)
    private Restaurant restaurant;

    @Column(nullable=false,length=100)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(name="display_order",nullable=false)
    @Builder.Default
    private Short displayOrder = 0;

    @Column(name="is_active",nullable=false)
    @Builder.Default
    private boolean isActive = true;
}
