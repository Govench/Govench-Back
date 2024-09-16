package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.UserResponseDTO;
import com.upao.govench.govench.model.dto.UserRequestDTO;
import com.upao.govench.govench.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
@Service
public interface UserService {
    List<UserResponseDTO> getAllUsers();

    User createUser(UserRequestDTO userRequestDTO);
    User updateUser(Integer userId, UserRequestDTO userDTO);
    User getUserbyId(Integer userId);
    Optional<User> findByEmail(String email);
    void deleteUser(Integer userId);
    boolean authenticateUser(String email, String password);
    void followUser(Integer userId, Integer followerId);
    void removeFollowUser(Integer userId, Integer followedUserId);
}
