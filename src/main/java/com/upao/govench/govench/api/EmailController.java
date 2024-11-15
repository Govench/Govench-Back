package com.upao.govench.govench.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import com.upao.govench.govench.model.dto.EmailRequestDTO;
import com.upao.govench.govench.service.PasswordResetService;
import com.upao.govench.govench.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/email")

public class EmailController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable("email") String email) {
        return passwordResetService.initiatePasswordReset(email);
    }

    @PostMapping("/reset-password/{token}/{newPass}")
    public ResponseEntity<String> resetPassword(@PathVariable("token") String token, @PathVariable("newPass") String newPassword) {
        return passwordResetService.resetPassword(token, newPassword);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validation(@RequestParam String token) {
        boolean isValid = passwordResetService.tokenValidation(token);
        return ResponseEntity.ok(isValid);
    }
}