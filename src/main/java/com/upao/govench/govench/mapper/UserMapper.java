package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.OwnerResponseDTO;
import com.upao.govench.govench.model.dto.UserRequestDTO;
import com.upao.govench.govench.model.dto.UserResponseDTO;
import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper {
    private ModelMapper modelMapper;

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
