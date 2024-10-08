package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.RatingEventRequestDTO;
import com.upao.govench.govench.model.dto.RatingEventResponseDTO;
import com.upao.govench.govench.model.dto.UserResponseDTO;
import com.upao.govench.govench.model.dto.UserRequestDTO;
import com.upao.govench.govench.model.entity.Rating;
import com.upao.govench.govench.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
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
    User associateProfileWithUser(int userId, String profileId);
    User dessasociateProfileWithUser(int userId);
    void followUser(Integer userId, Integer followerId);
    void removeFollowUser(Integer userId, Integer followedUserId);

    void rateUser(Integer raterUserId, Integer ratedUserId, Integer ratingValue, String comment);
    List<Rating> getUserRatings(Integer userId);

    RatingEventResponseDTO createRatingEvent(int  userId, int eventId, RatingEventRequestDTO ratingEventRequestDTO);
}
