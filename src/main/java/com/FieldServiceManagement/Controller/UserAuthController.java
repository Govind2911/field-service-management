package com.FieldServiceManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FieldServiceManagement.DTO.AuthResponseDTO;
import com.FieldServiceManagement.DTO.ForgotPasswordDTO;
import com.FieldServiceManagement.DTO.LoginRequestDTO;
import com.FieldServiceManagement.DTO.RegisterRequestDTO;
import com.FieldServiceManagement.DTO.ResetPasswordDTO;
import com.FieldServiceManagement.Service.UserAuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user_auth")
@RequiredArgsConstructor
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequestDTO register) {

        return ResponseEntity.ok(
                userAuthService.register(register)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO login) {

        return ResponseEntity.ok(
                userAuthService.login(login)
        );
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordDTO forgotPassword) {

        userAuthService.forgotPassword(
                forgotPassword.getUserEmail()
        );

        return ResponseEntity.ok(
                "Reset mail sent on your Email"
        );
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordDTO resetPassword) {

        userAuthService.resetPassword(
                resetPassword.getToken(),
                resetPassword.getNewPassword()
        );

        return ResponseEntity.ok(
                "Password reset successfully"
        );
    }

    @PostMapping("/loggedOut")
    public ResponseEntity<String> loggedOut(
            HttpServletRequest request) {

        return ResponseEntity.ok(
                userAuthService.logout(request)
        );
    }
}