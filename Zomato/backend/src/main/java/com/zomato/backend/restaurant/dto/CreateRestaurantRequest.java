package com.zomato.backend.restaurant.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRestaurantRequest {
    
    @NotBlank(message="Restaurant name is required")
    @Size(min=2,max=255,message="Name must be between 2 and 255 characters")
    private String name;

    @Size(max=1000,message="Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message="Phone number is required")
    @Pattern(regexp="^\\+?[0-9]{10,15}$",message="Invalid phone number")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message="Address line 1 is required")
    private String addressLine1;

    private String addressLine2;
    private String landmark;

    @NotBlank(message="City is required")
    private String city;

    @NotBlank(message="State is required")
    private String state;

    @NotBlank(message="Pincode is required")
    @Pattern(regexp="^[0-9]{6}$",message="Pincode must be 6 digits")

    @NotNull(message="Latitude is required")
    @DecimalMin(value="-90.0",message="Invalid Latitude")
    @DecimalMax(value="90.0",message="Invalid Latitude")
    private BigDecimal latitude;

    @NotNull(message="Longitude is required")
    @DecimalMin(value="-180.0",message="Invalid longitude")
    @DecimalMax(value="180.0",message="Invalid longitude")
    private BigDecimal longitude;

    @
}
