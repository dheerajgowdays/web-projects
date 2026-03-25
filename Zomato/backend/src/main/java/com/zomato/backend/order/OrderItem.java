package com.zomato.backend.order;

import com.zomato.backend.common.enums.FoodType;
import com.zomato.backend.menu.MenuItem;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id",nullable = false)
    private MenuItem menuItem;

    @Column(name = "item_name",nullable = false,length=255)
    private String itemName;

    @Column(name = "item_price_paise",nullable = false)
    private Integer itemPricePaise;

    @Column(name = "line_total_paise;",nullable = false)
    private Integer lineTotalPaise;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_type",nullable = false,length = 10)
    private FoodType foodType;

    @Column(nullable = false)
    private Short quantity;

    @Column(name= "customization_note",columnDefinition = "TEXT")
    private String customizationNote;
}
