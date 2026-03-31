package com.zomato.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zomato.backend.auth.dto.*;
import com.zomato.backend.auth.dto.AuthResponse;
import com.zomato.backend.auth.dto.RefreshTokenRequest;
import com.zomato.backend.common.response.ApiResponse;
import com.zomato.backend.user.User;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
        @Valid @RequestBody RegisterRequest request){
            AuthResponse response = authService.register(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully",response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletRequest httpRequest
    ){
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = getClientIp(httpRequest);

        AuthResponse response = authService.login(request,deviceInfo,ipAddress);
        return ResponseEntity.ok(ApiResponse.success("Login successful",response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
        @Valid @RequestBody RefreshTokenRequest request
    ){
        AuthResponse response = authService.refreshAccessToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
        @Valid @RequestBody RefreshTokenRequest request){
            authService.logout(request.getRefteshToken());
            return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
        @AuthenticationPrincipal User currentUser
    ){
        authService.logoutAllDevices(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("All device logged out"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> getCurrentUser(
        @AuthenticationPrincipal User currentUser
    ){
        return ResponseEntity.ok(ApiResponse.success("Current user",currentUser));
    }

    private String getClientIp(HttpServletRequest request){

        String forwarded = request.getHeader("X-Forwarded-For");
        if(forwarded != null && !forwarded.isBlank()){
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
