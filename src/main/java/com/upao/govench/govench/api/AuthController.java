package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.AuthResponseDTO;
import com.upao.govench.govench.model.dto.LoginDTO;
import com.upao.govench.govench.model.dto.UserProfileDTO;
import com.upao.govench.govench.model.dto.UserRegistrationDTO;
import com.upao.govench.govench.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/register/participant")
    public ResponseEntity<UserProfileDTO> registerParticipant(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserProfileDTO userProfileDTO = userService.registerParticipant(userRegistrationDTO);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.CREATED);
    }

    @PostMapping("/register/organizer")
    public ResponseEntity<UserProfileDTO> registerOrganizer(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UserProfileDTO userProfileDTO = userService.registerOrganizer(userRegistrationDTO);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponseDTO authResponseDTO = userService.login(loginDTO);
        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }
}

