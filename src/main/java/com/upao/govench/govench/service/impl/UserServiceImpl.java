package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.ResourceNotFoundException;
import com.upao.govench.govench.mapper.RatingEventMapper;
import com.upao.govench.govench.mapper.UserMapper;
import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.*;
import com.upao.govench.govench.repository.*;
import com.upao.govench.govench.security.TokenProvider;
import com.upao.govench.govench.security.UserPrincipal;
import com.upao.govench.govench.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    //
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private  RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingEventMapper ratingEventMapper;

    @Autowired
    private RatingEventRepository ratingEventRepository;

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private FollowRepository followRepository;
    private EventRepository eventRepository;


    //Metodos seguridad //

    @Override
    public UserProfileDTO registerParticipant(UserRegistrationDTO userRegistrationDTO) {
        Role role = roleRepository.findById(2).orElse(null);
        return UserRegistrationWithRole(userRegistrationDTO,role);

    }

    @Override
    public UserProfileDTO registerOrganizer(UserRegistrationDTO userRegistrationDTO) {
        Role role = roleRepository.findById(3).orElse(null);
        return UserRegistrationWithRole(userRegistrationDTO,role);
    }


    @Override
    public UserProfileDTO uptadteUserProfile(Integer id, UserProfileDTO userProfileDTO) {
        User user = userRepository.findById(id).orElseThrow( ( )-> new ResourceNotFoundException("Usuario no encontrado"));

        boolean existParticipant = participantRepository.existsByNameAndLastnameAndUserIdNot(userProfileDTO.getName(),userProfileDTO.getLastname(),id);
        boolean existOrganizer = organizerRepository.existsByNameAndLastnameAndUserIdNot(userProfileDTO.getName(),userProfileDTO.getLastname(),id);

        if (existParticipant || existOrganizer) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo nombre y apellido");
        }

        if(user.getParticipant()!=null)
        {
            if(userProfileDTO.getName()!=null)user.getParticipant().setName(userProfileDTO.getName());
            if(userProfileDTO.getLastname()!=null)user.getParticipant().setLastname(userProfileDTO.getLastname());
            if(userProfileDTO.getProfileDesc()!=null) user.getParticipant().setProfileDesc(userProfileDTO.getProfileDesc());
            if(userProfileDTO.getGender()!=null) user.getParticipant().setGender(userProfileDTO.getGender());
            if(userProfileDTO.getBirthday()!=null) user.getParticipant().setBirthday(userProfileDTO.getBirthday());
            if(userProfileDTO.getInterest()!=null) user.getParticipant().setInterest(userProfileDTO.getInterest());
            if(userProfileDTO.getSkills()!=null) user.getParticipant().setSkills(userProfileDTO.getSkills());
            if(userProfileDTO.getSocialLinks()!=null)user.getParticipant().setSocialLinks(userProfileDTO.getSocialLinks());

        }

        if(user.getOrganizer()!=null)
        {
            if(userProfileDTO.getName()!=null)user.getOrganizer().setName(userProfileDTO.getName());
            if(userProfileDTO.getLastname()!=null)user.getOrganizer().setLastname(userProfileDTO.getLastname());
            if(userProfileDTO.getProfileDesc()!=null) user.getOrganizer().setProfileDesc(userProfileDTO.getProfileDesc());
            if(userProfileDTO.getGender()!=null) user.getOrganizer().setGender(userProfileDTO.getGender());
            if(userProfileDTO.getBirthday()!=null) user.getOrganizer().setBirthday(userProfileDTO.getBirthday());
            if(userProfileDTO.getInterest()!=null) user.getOrganizer().setInterest(userProfileDTO.getInterest());
            if(userProfileDTO.getSkills()!=null) user.getOrganizer().setSkills(userProfileDTO.getSkills());
            if(userProfileDTO.getSocialLinks()!=null)user.getOrganizer().setSocialLinks(userProfileDTO.getSocialLinks());

        }

        User updatedUser = userRepository.save(user);
        return userMapper.toUserProfileDTO(updatedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDTO getUserProfilebyId(Integer id) {
        User user = userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toUserProfileDTO(user);
    }

    private UserProfileDTO UserRegistrationWithRole(UserRegistrationDTO userRegistrationDTO,Role role) {
        boolean existByEmail = userRepository.existsByEmail(userRegistrationDTO.getEmail());
        boolean existOrganizer = organizerRepository.existsByNameAndLastname(userRegistrationDTO.getName(), userRegistrationDTO.getLastname());
        boolean existParticipant = participantRepository.existsByNameAndLastname(userRegistrationDTO.getName(), userRegistrationDTO.getLastname());

        if (existByEmail) {
            throw new IllegalArgumentException("Email ya esta registrado");
        }
        if (existOrganizer || existParticipant) {
            throw new IllegalArgumentException("El usuario ya esta registrado");
        }

        userRegistrationDTO.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        User user = userMapper.toUserEntity(userRegistrationDTO);
        user.setRole(role);
        if (Objects.equals(role.getName(), "ROLE_PARTICIPANT")) {
            Participant participant = new Participant();
            participant.setName(userRegistrationDTO.getName());
            participant.setLastname(userRegistrationDTO.getLastname());
            participant.setBirthday(userRegistrationDTO.getBirthday());
            participant.setGender(userRegistrationDTO.getGender());
            participant.setProfileDesc(userRegistrationDTO.getProfileDesc());
            participant.setInterest(userRegistrationDTO.getInterest());
            participant.setSkills(userRegistrationDTO.getSkills());
            participant.setSocialLinks(userRegistrationDTO.getSocialLinks());
            participant.setCreated(LocalDateTime.now());
            participant.setUser(user);
            user.setParticipant(participant);
        } else if (Objects.equals(role.getName(), "ROLE_ORGANIZER")) {
            Organizer organizer = new Organizer();
            organizer.setName(userRegistrationDTO.getName());
            organizer.setLastname(userRegistrationDTO.getLastname());
            organizer.setBirthday(userRegistrationDTO.getBirthday());
            organizer.setGender(userRegistrationDTO.getGender());
            organizer.setProfileDesc(userRegistrationDTO.getProfileDesc());
            organizer.setInterest(userRegistrationDTO.getInterest());
            organizer.setSkills(userRegistrationDTO.getSkills());
            organizer.setSocialLinks(userRegistrationDTO.getSocialLinks());
            organizer.setCreated(LocalDateTime.now());
            organizer.setEventosCreados(0);
            organizer.setUser(user);
            user.setOrganizer(organizer);
        }
        user.setPremiun(false);
        User savedUser = userRepository.save(user);
        return userMapper.toUserProfileDTO(savedUser);
    }

    @Override
    public AuthResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword())

        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userPrincipal.getUser();

        //Aca se va generar el token

        String token = tokenProvider.createAccessToken(authentication);

        AuthResponseDTO responseDTO = userMapper.toAuthResponseDTO(user,token);
        return responseDTO;
    }

    public Integer getAuthenticatedUserIdFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String token = (String) authentication.getCredentials(); // Obtén el token del objeto de autenticación

            // Extraer el email del token
            Claims claims = tokenProvider.getJwtParser().parseClaimsJws(token).getBody();
            String email = claims.getSubject();


            // Buscar el usuario usando el email
            User user = userRepository.findByEmail(email).orElse(null); // Debes implementar este método en tu UserService
            return user != null ? user.getId() : null;
        }
        return null; // Si no hay autenticación, devuelve null
    }

    @Override
    public void SubscribePremium(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        user.setPremiun(true);
        userRepository.save(user);
    }

    @Override
    public void DesubscribePremium() {
        Integer id = this.getAuthenticatedUserIdFromJWT();
        User user = userRepository.findById(id).orElse(null);
        if(!user.getPremium())
        {
            throw new IllegalArgumentException("El usuario no es premium");
        }
        user.setPremiun(false);
        userRepository.save(user);
    }

    ///--------Metodos pre seguridad----------///
    @Override
    public User getUserbyId(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuario no encontrado");
        } userRepository.deleteById(userId);
    }


    @Override
    public List<UserProfileDTO> getAllUsers() {
        List<User> users = userRepository.findAll(); // Obtén todos los usuarios desde la base de datos
        return userMapper.getAllUsers(users); // Usa el método de tu mapper para convertirlos a UserProfileDTO
    }



    @Override
    public User associateProfileWithUser(int userId, String profileId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user ==null)
        {
            throw new NullPointerException("Usuario no encontrado");
        }
        if (user.getParticipant() != null) {
            user.getParticipant().setProfileId(profileId);
            return userRepository.save(user);
        }
        if (user.getOrganizer() != null) {
            user.getOrganizer().setProfileId(profileId);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User dessasociateProfileWithUser(int userId) {

        User user = userRepository.findById(userId).orElse(null);
        if(user ==null)
        {
            throw new NullPointerException("Usuario no encontrado");
        }


        if (user.getParticipant() != null) {

            String profileId = user.getParticipant().getProfileId();


            if (profileId != null) {
                user.getParticipant().setProfileId(null);
                return userRepository.save(user);
            }
        }

        if (user.getOrganizer() != null) {

            String profileId = user.getOrganizer().getProfileId();


            if (profileId != null) {
                user.getOrganizer().setProfileId(null);
                return userRepository.save(user);
            }
        }

        return null;
    }

     @Override
     public void rateUser(Integer raterUserId, Integer ratedUserId, Integer ratingValue, String comment) {
         User rater = userRepository.findById(raterUserId)
                 .orElseThrow(() -> new NotFoundException("Usuario que califica no encontrado"));

         User rated = userRepository.findById(ratedUserId)
                 .orElseThrow(() -> new NotFoundException("Usuario a calificar no encontrado"));
         if(rater==rated)
         {
             throw new IllegalArgumentException("No puedes calificarte a ti mismo.");
         }
         Rating rating = new Rating();
         rating.setRatingValue(ratingValue);
         rating.setComment(comment);

         rating.setRatedUser(rated);
         rating.setRaterUser(rater);

         // Guardar la calificación
         ratingRepository.save(rating);
     }

    @Override
    public List<Rating> getUserRatings(Integer userId) { //buscar los que yo he calificado

        return ratingRepository.findAllByRaterUser_Id(userId);
    }

    @Override
    public List<Rating> getUserRated(Integer userId) { //buscar quienes me han calificado

        return ratingRepository.findAllByRatedUser_Id(userId);
    }


    @Override
    public RatingEventResponseDTO createRatingEvent(User user, Event event, RatingEventRequestDTO ratingEventRequestDTO) {

        boolean userAsist = userEventRepository.existsByUserAndEvent(user, event);
        if(!userAsist) {
            throw new RuntimeException("Usuario no asistio al evento");
        }

        boolean userRating = ratingEventRepository.existsByUserIdAndEventId(user, event);
        if(userRating) {
            throw new RuntimeException("El usuario ya ha calificado ese evento");
        }


        System.out.print(ratingEventRequestDTO);

        RatingEvent ratingEvent = ratingEventMapper.convertToEntity(ratingEventRequestDTO);
        ratingEvent.setUserId(user);
        ratingEvent.setEventId(event);
        ratingEvent.setFechaPuntuacion(LocalDate.now());
        ratingEventRepository.save(ratingEvent);
        return ratingEventMapper.convertToDTO(ratingEvent);
    }


    //userFollower - Seguidor
    //userFollowing - Seguido //usuario base
    @Override
    public void followUser(Integer followedUserId) {
        Integer userId = getAuthenticatedUserIdFromJWT();

        User userFollower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User userFollowing = userRepository.findById(followedUserId)
                .orElseThrow(() -> new RuntimeException("Usuario a seguir no encontrado"));

        if (userFollower.getId() == userFollowing.getId()) {
            throw new IllegalArgumentException("No puedes seguirte a ti mismo.");
        }
        // Verificar si la relación de seguimiento ya existe
        if (followRepository.existsByFollowerAndFollowing(userFollower, userFollowing)) {
            throw new IllegalArgumentException("Ya sigues a este usuario.");
        }


        Follow follow = new Follow();
        follow.setFollower(userFollower);
        follow.setFollowing(userFollowing);
        followRepository.save(follow);
    }

    @Override
    public void unfollowUser(Integer followedUserId) {
        Integer userId = getAuthenticatedUserIdFromJWT();

        User userFollower = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User userFollowing = userRepository.findById(followedUserId)
                .orElseThrow(() -> new RuntimeException("Usuario a seguir no encontrado"));

        // Verificar si la relación de seguimiento existe
        if (!followRepository.existsByFollowerAndFollowing(userFollower, userFollowing)) {
            throw new IllegalArgumentException("No puedes dejar de seguir a quien no sigues.");
        }
        Follow follow=followRepository.findByFollowerAndFollowing(userFollower, userFollowing);

        followRepository.delete(follow);

    }

    @Override
    public int getFollowersCount() {

        Integer userId = getAuthenticatedUserIdFromJWT();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return followRepository.findByFollowing(user).size();
    }

    @Override
    public int getFollowingCount() {

        Integer userId = getAuthenticatedUserIdFromJWT();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return followRepository.findByFollower(user).size();
    }

    @Override
    public List<FollowResponseDTO> getFollowers() {
        Integer userId = getAuthenticatedUserIdFromJWT();

        return userMapper.converToListFollowDTO(followRepository.findByFollowing_Id(userId));
    }

    @Override
    public List<FollowResponseDTO> getFollowings() {
        Integer userId = getAuthenticatedUserIdFromJWT();

        return userMapper.converToListFollowedDTO(followRepository.findByFollower_Id(userId));
    }

    @Override
    public String updatePassword(PasswordDTO passwordDTO) {
        Integer userId = getAuthenticatedUserIdFromJWT();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Contraseña actual incorrecta");
        }

        if (passwordDTO.getNewPassword() == null || passwordDTO.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("Nueva contraseña es requerida");
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("La nueva contraseña y la confirmación no coinciden.");
        }

        user = userMapper.updateUserPassword(user, passwordDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "Contraseña Actualizada";
    }
}