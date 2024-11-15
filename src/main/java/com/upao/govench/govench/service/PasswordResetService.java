package com.upao.govench.govench.service;

import com.upao.govench.govench.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.upao.govench.govench.model.entity.PasswordResetToken;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.PasswordResetTokenRepository;
import com.upao.govench.govench.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    public ResponseEntity<String> initiatePasswordReset(String email) {
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmailQuery(email);
            try {
                PasswordResetToken recoveryToken = tokenRepository.findByToken(email);
                if (recoveryToken != null && recoveryToken.getExpiryDate().isAfter(LocalDateTime.now().minusHours(1))) {
                    // Si el token ya existe y no ha expirado, se devuelve un mensaje de error
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya se envió un correo de recuperación a esta cuenta, intente de nuevo en 1 hora.");
                }

                String token = UUID.randomUUID().toString();
                PasswordResetToken passwordResetToken = new PasswordResetToken();
                passwordResetToken.setToken(token);
                passwordResetToken.setUser(user);
                passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
                tokenRepository.save(passwordResetToken);

                String titleEmail = "¡Hola, Somos Govench!";
                String messageEmail = "\n\nHola, solicitaste un restablecimiento de contraseña para tu cuenta "
                        + email
                        +", \na continuación copia y pega el siguiente token en el formulario para continuar."
                        +"\nSi has sido tú, ponte en contacto de inmediato con nuestro equipo.";
                String resetLink = titleEmail + messageEmail +" \n\nTOKEN: " + token + "\n\n"+ notificationServiceImpl.generateSignature();
                emailService.sendEmail(user.getEmail(), "Solicitud de cambio de contraseña \uD83D\uDD12", resetLink);
                return ResponseEntity.status(HttpStatus.CREATED).body("Token enviado exitosamente");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo de recuperación.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El correo no se encuentra registrado.");
    }

    public ResponseEntity<String> resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
        }

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(passwordResetToken);
            return ResponseEntity.status(HttpStatus.GONE).body("Token expirado, vuelva a solicitar uno nuevo");
        }

        User user = passwordResetToken.getUser();
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("La nueva contraseña no puede ser igual a la contraseña actual");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        tokenRepository.delete(passwordResetToken);

        return ResponseEntity.status(HttpStatus.OK).body("Contraseña restablecida exitosamente");
    }

    public boolean tokenValidation(String token){
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);
        return passwordResetToken != null && passwordResetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }
}