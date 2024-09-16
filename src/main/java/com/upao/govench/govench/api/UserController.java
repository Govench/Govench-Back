package com.upao.govench.govench.api;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.model.dto.LoginRequestDTO;
import com.upao.govench.govench.model.dto.UserRequestDTO;
import com.upao.govench.govench.model.dto.UserResponseDTO;
import com.upao.govench.govench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    private LoginRequestDTO loginRequestDTO;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            userService.createUser(userRequestDTO);
            return new ResponseEntity<>("Cuenta creada con éxito", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al obtener la lista de usuarios", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Integer id, @RequestBody UserRequestDTO userRequestDTO) {
        try {
            userService.updateUser(id, userRequestDTO);
            return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            boolean isAuthenticated = userService.authenticateUser(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            if (isAuthenticated) {
                return new ResponseEntity<>("Inicio de sesión exitoso", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Correo electrónico o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error en el sistema", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/follow/{followedUserId}")
    public ResponseEntity<String> followUser(@PathVariable Integer userId, @PathVariable Integer followedUserId) {
        try {
            userService.followUser(userId, followedUserId);
            return new ResponseEntity<>("Usuario seguido correctamente", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Error al seguir al usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/unfollow/{removedUserId}")
    public ResponseEntity<String> removeFollowUser(@PathVariable Integer userId, @PathVariable Integer removedUserId) {
        try {
            userService.removeFollowUser(userId, removedUserId);
            return new ResponseEntity<>("Dejado de seguir Usuario correctamente", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>("Error al dejar de seguir al usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
