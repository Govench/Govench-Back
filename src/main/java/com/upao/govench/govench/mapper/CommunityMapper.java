package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.CommunityRequestDTO;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.entity.Community;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CommunityMapper {

    private final ModelMapper modelMapper;

    public Community convertToEntity(CommunityRequestDTO communityRequestDTO)
    {
        return modelMapper.map(communityRequestDTO, Community.class);
    }
    public Community convertToEntity(CommunityResponseDTO communityResponseDTO)
    {
        return modelMapper.map(new CommunityResponseDTO(), Community.class);
    }
    public CommunityResponseDTO converToResponseDTO(Community community) {
        if (community == null) {
            throw new IllegalArgumentException("El objeto comunidad no puede ser nulo");
        }
        return modelMapper.map(community, CommunityResponseDTO.class);
    }
    public CommunityRequestDTO converToRequestDTO(Community community){
        return modelMapper.map(community, CommunityRequestDTO.class);
    }
    public List<CommunityResponseDTO> convertoToListResponseDTO(List<Community> communities){
        return communities.stream().map(this::converToResponseDTO).toList();
    }
}
