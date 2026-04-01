package com.zomato.backend.restaurant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    

}
