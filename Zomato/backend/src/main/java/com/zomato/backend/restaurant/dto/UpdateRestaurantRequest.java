package com.zomato.backend.restaurant.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRestaurantRequest {
    
    @Size(min=2,max=255)
    private String name;

    @Size(max=1000)
    private String description;

    @Pattern(regexp="^\\+?[0-9]{10,15}$",message="Invalid phone number")
    private String phone;

    @Email
    private String email;

    private String addressLine1;
    private String addressLine2;
    private String landmark;
    private String city;
    private String state;

    @Pattern(regexp="^[0-9]{6}$",message="Pincode must be 6 digits")
    private String pincode;

    @DecimalMin("-90.0") @DecimalMax("90.0")
    private BigDecimal latitude;

    @DecimalMin("-180.0") @DecimalMax("180.0")
    private BigDecimal longitude;

    @Min(1) @Max(3)
    private Short pricRange;

    @Min(0)
    private Integer minOrderPaise;

    @Min(0)
    private Integer deliveryFeePaise;

    @Min(1) @Max(180)
    private Short deliveryTimeMin;

    @Min(1) @Max(180)
    private Short deliveryTimeMax;

    @Size(max=20)
    private String fssaiLicense;

    private List<Integer> cuisineIds;

    private Boolean isOpen;

}
