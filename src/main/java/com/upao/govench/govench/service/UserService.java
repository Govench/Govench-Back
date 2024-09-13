package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.UserDTO;
import com.upao.govench.govench.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();

    User createUser(UserDTO userDTO);
    User updateUser(Integer userId, UserDTO userDTO);
    User getUserbyId(Integer userId);
    Optional<User> findByEmail(String email);

    void deleteUser(Integer userId);
}
