package com.upao.govench.govench.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.upao.govench.govench.model.entity.PasswordResetToken;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.PasswordResetTokenRepository;
import com.upao.govench.govench.repository.UserRepository;
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

    public String initiatePasswordReset(String email) {
        if (userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmailQuery(email);
            try {
                String token = UUID.randomUUID().toString();
                PasswordResetToken passwordResetToken = new PasswordResetToken();
                passwordResetToken.setToken(token);
                passwordResetToken.setUser(user);
                passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
                tokenRepository.save(passwordResetToken);

                String titleEmail = "¡Hola, Somos Govench!";
                String messageEmail = "\n\nHola, solicitaste un restablecimiento de contraseña para tu cuenta "+ email +", \na continuación copia y pega el siguiente token en el formulario para continuar.";
                String resetLink = titleEmail + messageEmail +" \n\nTOKEN: " + token;
                //Para restablecimiento de contraseña, pero presionando el enlace
                //String resetLink = "http://localhost:8080/reset-password?token=" + token;
                //String emailBody = titleEmail + messageEmail + "\n\n" + resetLink;
                emailService.sendEmail(user.getEmail(), "Password Reset Request \uD83D\uDD12", resetLink);
                return "Token enviado exitosamente";
            } catch (Exception e) {
                e.printStackTrace();
                return "Token no enviado "+e.getMessage();
            }
        }
        return "Token no encontrado";
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);
        if (passwordResetToken != null) {
            User user = passwordResetToken.getUser();
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);
            tokenRepository.delete(passwordResetToken);
        } else {
            throw new RuntimeException("Invalid or expired password reset token");
        }
    }
}