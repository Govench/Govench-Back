package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.dto.UserDTO;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public User getUserbyId(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User createUser(UserDTO userDTO) {

        if (!EMAIL_PATTERN.matcher(userDTO.getEmail()).matches()) {
            throw new IllegalArgumentException("El formato del correo es inválido");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está en uso");
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setBirthday(userDTO.getBirthday());
        user.setGender(userDTO.getGender());
        user.setProfileDesc(userDTO.getProfileDesc());
        user.setInterest(userDTO.getInterest());
        user.setSkills(userDTO.getSkills());
        user.setSocialLinks(userDTO.getSocialLinks());
        user.setFollowers(userDTO.getFollowers());
        user.setFollowed(userDTO.getFollowed());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = optionalUser.get();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        user.setPassword(userDTO.getPassword());

        user.setProfileDesc(userDTO.getProfileDesc());
        user.setInterest(userDTO.getInterest());
        user.setSkills(userDTO.getSkills());
        user.setSocialLinks(userDTO.getSocialLinks());
        user.setFollowers(userDTO.getFollowers());
        user.setFollowed(userDTO.getFollowed());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setPassword(user.getPassword());
                    dto.setBirthday(user.getBirthday());
                    dto.setGender(user.getGender());
                    dto.setProfileDesc(user.getProfileDesc());
                    dto.setInterest(user.getInterest());
                    dto.setSkills(user.getSkills());
                    dto.setSocialLinks(user.getSocialLinks());
                    dto.setFollowers(user.getFollowers());
                    dto.setFollowed(user.getFollowed());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
