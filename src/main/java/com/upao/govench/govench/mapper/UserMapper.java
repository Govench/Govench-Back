package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.*;
import com.upao.govench.govench.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;



    public User toUserEntity(UserRegistrationDTO registrationDTO) {
        return modelMapper.map(registrationDTO, User.class);
    }

    public UserProfileDTO toUserProfileDTO(User user) {
        UserProfileDTO userProfileDTO =  modelMapper.map(user, UserProfileDTO.class);
        if(user.getParticipant()!=null)
        {
            userProfileDTO.setName(user.getParticipant().getName());
            userProfileDTO.setLastname(user.getParticipant().getLastname());
            userProfileDTO.setProfileDesc(user.getParticipant().getProfileDesc());
            userProfileDTO.setInterest(user.getParticipant().getInterest());
            userProfileDTO.setSkills(user.getParticipant().getSkills());
            userProfileDTO.setSocialLinks(user.getParticipant().getSocialLinks());
        }
        if(user.getOrganizer()!=null)
        {
            userProfileDTO.setName(user.getOrganizer().getName());
            userProfileDTO.setLastname(user.getOrganizer().getLastname());
            userProfileDTO.setProfileDesc(user.getOrganizer().getProfileDesc());
            userProfileDTO.setInterest(user.getOrganizer().getInterest());
            userProfileDTO.setSkills(user.getOrganizer().getSkills());
            userProfileDTO.setSocialLinks(user.getOrganizer().getSocialLinks());
            userProfileDTO.setEventosCreados(user.getOrganizer().getEventosCreados());
        }
        if(user.getAdmin()!=null)
        {

        }
        return userProfileDTO;

    }


    public User toUserEntity(LoginDTO loginDTO)
    {
        return modelMapper.map(loginDTO, User.class);
    }

    public AuthResponseDTO toAuthResponseDTO(User user , String token) {
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(token);

        String name = (user.getParticipant() != null) ? user.getParticipant().getName() :
                (user.getOrganizer() != null) ? user.getOrganizer().getName():"ADMIN";
        String lastname = (user.getParticipant() != null ) ? user.getParticipant().getLastname() :
                (user.getOrganizer() != null) ? user.getOrganizer().getLastname():"USER";

        authResponseDTO.setName(name);
        authResponseDTO.setLastname(lastname);

        authResponseDTO.setRole(user.getRole().getName());

        return authResponseDTO;
    }


//metodos pre security//
    public User convertToEntity(UserRequestDTO userRequestDTO) {
        return modelMapper.map(userRequestDTO, User.class);
    }

    public UserResponseDTO convertToDTO(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public List<UserResponseDTO> convertToListDTO(List<User> users) {
        return users.stream()
                .map(this::convertToDTO)
                .toList();
    }
    public User convertToEntity(OwnerResponseDTO ownerResponseDTO) {
        return modelMapper.map(ownerResponseDTO, User.class);
    }
    public UserRequestDTO convertToRequestDTO(User user) {
        return modelMapper.map(user, UserRequestDTO.class);
    }
}
