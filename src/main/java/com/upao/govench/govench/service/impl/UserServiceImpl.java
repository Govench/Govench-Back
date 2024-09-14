package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.mapper.UserMapper;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.dto.UserResponseDTO;
import com.upao.govench.govench.model.dto.UserRequestDTO;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserbyId(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está en uso");
        } User user = userMapper.convertToEntity(userRequestDTO);
        return userRepository.save(user);
    }

    @Override
    public UserResponseDTO updateUser(Integer userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el ID " + userId));
        User updatedUser = userMapper.convertToEntity(userRequestDTO);
        updatedUser.setId(user.getId());
        updatedUser = userRepository.save(updatedUser);
        return userMapper.convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado");
        } userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.convertToListDTO(users);
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        } return false;
    }
}
