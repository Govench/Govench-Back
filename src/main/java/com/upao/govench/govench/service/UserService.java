package com.upao.govench.govench.service;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.Event;
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

    UserProfileDTO registerParticipant(UserRegistrationDTO userRegistrationDTO);
    UserProfileDTO registerOrganizer(UserRegistrationDTO userRegistrationDTO);
    UserProfileDTO uptadteUserProfile(Integer id, UserProfileDTO userProfileDTO);
    UserProfileDTO getUserProfilebyId(Integer id);

    void SubscribePremium(Integer id);
    void DesubscribePremium();

    //Autenticar el login
    AuthResponseDTO login(LoginDTO loginDTO);
    Integer getAuthenticatedUserIdFromJWT();


    List<UserResponseDTO> getAllUsers();
    User getUserbyId(Integer userId);
    void deleteUser(Integer userId);
    User associateProfileWithUser(int userId, String profileId);
    User dessasociateProfileWithUser(int userId);

    void followUser(Integer followedUserId);
    void unfollowUser(Integer followedUserId);
    int getFollowersCount();
    int getFollowingCount();

    void rateUser(Integer raterUserId, Integer ratedUserId, Integer ratingValue, String comment);
    List<Rating> getUserRatings(Integer userId);
    RatingEventResponseDTO createRatingEvent(User  userId, Event eventId, RatingEventRequestDTO ratingEventRequestDTO);

    List<FollowResponseDTO> getFollowers();
    List<FollowResponseDTO> getFollowings();
}
