package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.UserMapper;
import com.upao.govench.govench.model.entity.Rating;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.dto.UserResponseDTO;
import com.upao.govench.govench.model.dto.UserRequestDTO;
import com.upao.govench.govench.repository.ProfileMongoRepository;
import com.upao.govench.govench.repository.RatingRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.ProfileService;
import com.upao.govench.govench.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.upao.govench.govench.model.entity.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingRepository ratingRepository;

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
    public User updateUser(Integer userId, UserRequestDTO userDTO) {
        User user1 = getUserbyId(userId);

        if (user1 == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if(userDTO.getName() != null) user1.setName(userDTO.getName());
        if(userDTO.getEmail() != null) user1.setEmail(userDTO.getEmail());
        if(userDTO.getPassword() != null) user1.setPassword(userDTO.getPassword());
        if(userDTO.getBirthday() != null) user1.setBirthday(userDTO.getBirthday());
        if(userDTO.getGender() != null) user1.setGender(userDTO.getGender());
        if(userDTO.getProfileDesc() != null) user1.setProfileDesc(userDTO.getProfileDesc());
        if(userDTO.getInterest() != null) user1.setInterest(userDTO.getInterest());
        if(userDTO.getSkills() != null) user1.setSkills(userDTO.getSkills());
        if(userDTO.getSocialLinks() != null) user1.setSocialLinks(userDTO.getSocialLinks());
        return userRepository.save(user1);
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

    @Override
    public void followUser(Integer userId, Integer followedUserId){

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User userToFollow = userRepository.findById(followedUserId).orElseThrow(() -> new ResourceNotFoundException("Usuario a seguir no encontrado"));

        user.getFollowings().add(userToFollow);
        userToFollow.getFollowers().add(user);

        userRepository.save(user);
        userRepository.save(userToFollow);
    }

    @Override
    public User associateProfileWithUser(int userId, String profileId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setProfileId(profileId);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User dessasociateProfileWithUser(int userId) {

        User user = userRepository.findById(userId).orElse(null);


        if (user != null) {

            String profileId = user.getProfileId();


            if (profileId != null) {
                user.setProfileId(null);
                return userRepository.save(user);
            }
        }

        return null;
    }
    @Override
    public void removeFollowUser(Integer userId, Integer followedUserId){

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User userToFollow = userRepository.findById(followedUserId).orElseThrow(() -> new ResourceNotFoundException("Usuario a dejar de seguir no encontrado"));

        user.getFollowings().remove(userToFollow);
        userToFollow.getFollowers().remove(user);

        userRepository.save(user);
        userRepository.save(userToFollow);
    }


     @Override
    public void rateUser(Integer raterUserId, Integer ratedUserId, Integer ratingValue, String comment) {
        User rater = userRepository.findById(raterUserId).orElseThrow(() -> new RuntimeException("Usuario que califica no encontrado"));
        User rated = userRepository.findById(ratedUserId).orElseThrow(() -> new RuntimeException("Usuario a calificar no encontrado"));

        Rating rating = new Rating();
        rating.setRaterUser(rater);
        rating.setRatedUser(rated);
        rating.setRatingValue(ratingValue);
        rating.setComment(comment);

        ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getUserRatings(Integer userId) {
        return ratingRepository.findByRatedUserId(userId);
    }

}


