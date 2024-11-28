package com.upao.govench.govench.service;


import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.Event;
import com.upao.govench.govench.model.entity.Rating;
import com.upao.govench.govench.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
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

    List<UserProfileDTO> getAllUsers();
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
    List<Rating> getUserRated (Integer userId);
    RatingEventResponseDTO createRatingEvent(User  userId, Event eventId, RatingEventRequestDTO ratingEventRequestDTO);

    List<FollowResponseDTO> getFollowers();
    List<FollowResponseDTO> getFollowings();

    String updatePassword(PasswordDTO passwordDTO);
}
