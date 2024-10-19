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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    //
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

    //
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingEventMapper ratingEventMapper;
    @Autowired
    private final RatingEventRepository ratingEventRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;
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
            if(userProfileDTO.getInterest()!=null) user.getParticipant().setInterest(userProfileDTO.getInterest());
            if(userProfileDTO.getSkills()!=null) user.getParticipant().setSkills(userProfileDTO.getSkills());
            if(userProfileDTO.getSocialLinks()!=null)user.getParticipant().setSocialLinks(userProfileDTO.getSocialLinks());

        }

        if(user.getOrganizer()!=null)
        {
            if(userProfileDTO.getName()!=null)user.getOrganizer().setName(userProfileDTO.getName());
            if(userProfileDTO.getLastname()!=null)user.getOrganizer().setLastname(userProfileDTO.getLastname());
            if(userProfileDTO.getProfileDesc()!=null) user.getOrganizer().setProfileDesc(userProfileDTO.getProfileDesc());
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

    private UserProfileDTO UserRegistrationWithRole(UserRegistrationDTO userRegistrationDTO,Role role)
    {
        boolean existByEmail = userRepository.existsByEmail(userRegistrationDTO.getEmail());
        boolean existOrganizer = organizerRepository.existsByNameAndLastname(userRegistrationDTO.getName(),userRegistrationDTO.getLastname());
        boolean existParticipant = participantRepository.existsByNameAndLastname(userRegistrationDTO.getName(),userRegistrationDTO.getLastname());

        if(existByEmail)
        {
            throw new IllegalArgumentException("Email ya esta registrado");
        }
        if(existOrganizer || existParticipant)
        {
            throw new IllegalArgumentException("El usuario ya esta registrado");
        }

       userRegistrationDTO.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        User user = userMapper.toUserEntity(userRegistrationDTO);
        user.setRole(role);

        if(Objects.equals(role.getName(), "ROLE_PARTICIPANT"))
        {   Participant participant = new Participant();
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

        }
        else if (Objects.equals(role.getName(), "ROLE_ORGANIZER")) {
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


    ///--------Metodos pre seguridad----------///
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
        if(userDTO.getName() != null) user1.getParticipant().setName(userDTO.getName());
        if(userDTO.getEmail() != null) user1.setEmail(userDTO.getEmail());
        if(userDTO.getPassword() != null) user1.setPassword(userDTO.getPassword());
        if(userDTO.getBirthday() != null) user1.getParticipant().setBirthday(userDTO.getBirthday());
        if(userDTO.getGender() != null) user1.getParticipant().setGender(userDTO.getGender());
        if(userDTO.getProfileDesc() != null) user1.getParticipant().setProfileDesc(userDTO.getProfileDesc());
        if(userDTO.getInterest() != null) user1.getParticipant().setInterest(userDTO.getInterest());
        if(userDTO.getSkills() != null) user1.getParticipant().setSkills(userDTO.getSkills());
        if(userDTO.getSocialLinks() != null) user1.getParticipant().setSocialLinks(userDTO.getSocialLinks());
        return userRepository.save(user1);
    }
    public User updateOrganizer(Integer userId, UserRequestDTO userDTO) {
        User user1 = getUserbyId(userId);

        if (user1 == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if(userDTO.getName() != null) user1.getOrganizer().setName(userDTO.getName());
        if(userDTO.getEmail() != null) user1.setEmail(userDTO.getEmail());
        if(userDTO.getPassword() != null) user1.setPassword(userDTO.getPassword());
        if(userDTO.getBirthday() != null) user1.getOrganizer().setBirthday(userDTO.getBirthday());
        if(userDTO.getGender() != null) user1.getOrganizer().setGender(userDTO.getGender());
        if(userDTO.getProfileDesc() != null) user1.getOrganizer().setProfileDesc(userDTO.getProfileDesc());
        if(userDTO.getInterest() != null) user1.getOrganizer().setInterest(userDTO.getInterest());
        if(userDTO.getSkills() != null) user1.getOrganizer().setSkills(userDTO.getSkills());
        if(userDTO.getSocialLinks() != null) user1.getOrganizer().setSocialLinks(userDTO.getSocialLinks());
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

        user.getParticipant().getFollowings().add(userToFollow.getParticipant());
        userToFollow.getParticipant().getFollowers().add(user.getParticipant());

        userRepository.save(user);
        userRepository.save(userToFollow);
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
    public void removeFollowUser(Integer userId, Integer followedUserId){

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        User userToFollow = userRepository.findById(followedUserId).orElseThrow(() -> new ResourceNotFoundException("Usuario a dejar de seguir no encontrado"));

        user.getParticipant().getFollowings().remove(userToFollow);
        userToFollow.getParticipant().getFollowers().remove(user);

        userRepository.save(user);
        userRepository.save(userToFollow);
    }


     @Override
     public void rateUser(Integer raterUserId, Integer ratedUserId, Integer ratingValue, String comment) {
         User rater = userRepository.findById(raterUserId)
                 .orElseThrow(() -> new RuntimeException("Usuario que califica no encontrado"));

         User rated = userRepository.findById(ratedUserId)
                 .orElseThrow(() -> new RuntimeException("Usuario a calificar no encontrado"));

         Rating rating = new Rating();
         rating.setRatingValue(ratingValue);
         rating.setComment(comment);

         // Establecer el rater y el rated según el tipo de usuario
         if (rater.getOrganizer() != null) {
             rating.setRaterOrganizer(rater.getOrganizer()); // Si el calificador es un organizador
         } else if (rater.getParticipant() != null) {
             rating.setRaterParticipant(rater.getParticipant()); // Si el calificador es un participante
         } else {
             throw new RuntimeException("El calificador debe ser un organizador o un participante.");
         }

         if (rated.getOrganizer() != null) {
             rating.setRatedOrganizer(rated.getOrganizer()); // Si el calificado es un organizador
         } else if (rated.getParticipant() != null) {
             rating.setRatedParticipant(rated.getParticipant()); // Si el calificado es un participante
         } else {
             throw new RuntimeException("El usuario a calificar debe ser un organizador o un participante.");
         }

         // Guardar la calificación
         ratingRepository.save(rating);
     }
    @Override
    public List<Rating> getUserRatings(Integer userId) {

        return ratingRepository.findAllById(userId);
    }

    @Override
    public RatingEventResponseDTO createRatingEvent(int userId, int eventId, RatingEventRequestDTO ratingEventRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario que califica no encontrado"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Evento a calificar no encontrado"));

        boolean userAsist = userEventRepository.existsByUserAndEvent(user, event);
        if(!userAsist) {
            throw new RuntimeException("Usuario no asistio al evento");
        }

        RatingEvent ratingEvent = ratingEventMapper.convertToEntity(ratingEventRequestDTO);
        ratingEvent.setUserId(userId);
        ratingEvent.setEventId(eventId);
        ratingEvent.setFechaPuntuacion(LocalDate.now());
        ratingEventRepository.save(ratingEvent);
        return ratingEventMapper.convertToDTO(ratingEvent);
    }

}


