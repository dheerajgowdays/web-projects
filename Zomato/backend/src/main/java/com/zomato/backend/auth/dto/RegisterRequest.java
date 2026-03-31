package com.zomato.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    
    @NotBlank(message="Full name is required")
    @Size(min=2,max=100,message="Name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message="Email is required")
    @Email(message="Please provide a vaild email address")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=8,message="Password mush be at least 8 characters")
    @Pattern(
        regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message="Password must contain uppercase, lowercase, and a number"
    )
    private String password;

    @Pattern (regexp="^\\+?[0-9]{10,15}$",message="Invalid phone number format")
    private String phone;
}
