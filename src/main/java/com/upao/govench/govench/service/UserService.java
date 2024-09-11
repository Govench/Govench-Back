package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.UserDTO;
import com.upao.govench.govench.model.entity.User;

import java.util.*;

public interface UserService {
    User createUser(UserDTO userDTO);
    User updateUser(Long userId, UserDTO userDTO);
    Optional<User> findByEmail(String email);
    List<UserDTO> getAllUsers();
    void deleteUser(Long userId);
}
