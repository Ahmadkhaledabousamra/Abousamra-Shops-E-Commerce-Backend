package com.ahmadcode.abousamrashops.controller;


import com.ahmadcode.abousamrashops.request.LoginRequest;
import com.ahmadcode.abousamrashops.response.ApiResponse;
import com.ahmadcode.abousamrashops.response.JwtResponse;
import com.ahmadcode.abousamrashops.security.jwt.JwtUtils;
import com.ahmadcode.abousamrashops.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils  jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try {
            Authentication authentication = authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(),jwt);
            return ResponseEntity.ok(new ApiResponse("Login Successful",jwtResponse));
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }
}
