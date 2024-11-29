package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.PasswordResetTokenService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/email")

public class EmailController {

    private final UserRepository userRepository;
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/sendMail")
    public ResponseEntity<?> sendPasswordResetMail(@RequestBody String email) {
        try {
            passwordResetTokenService.createAndSendPasswordResetToken(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo.");
        }
    }

    @PostMapping("/reset/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @RequestBody String newPassword) {
        try {
            return passwordResetTokenService.resetPassword(token, newPassword);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al restablecer la contraseña.");
        }
    }

    @GetMapping("/reset/check/{token}")
    public ResponseEntity<?> checkTokenValidity(@PathVariable("token") String token) {
        try {
            boolean isValid = passwordResetTokenService.isValidToken(token);
            return new ResponseEntity<>(isValid, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el token.");
        }
    }

    @GetMapping("/validation")
    public ResponseEntity<Boolean> emailExists(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}