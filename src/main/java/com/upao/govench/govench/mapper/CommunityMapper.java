package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.CommunityRequestDTO;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.entity.Community;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CommunityMapper {

    private final ModelMapper modelMapper;
    private final PostMapper postMapper;
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
        CommunityResponseDTO communityResponseDTO = modelMapper.map(community, CommunityResponseDTO.class);

        // Verificar si el propietario es participante u organizador
        if (community.getOwner().getParticipant() != null) {
            communityResponseDTO.getOwner().setName(community.getOwner().getParticipant().getName());
            communityResponseDTO.getOwner().setProfileDesc(community.getOwner().getParticipant().getProfileDesc());
        }
        if (community.getOwner().getOrganizer() != null) {
            communityResponseDTO.getOwner().setName(community.getOwner().getOrganizer().getName());
            communityResponseDTO.getOwner().setProfileDesc(community.getOwner().getOrganizer().getProfileDesc());
        }

        // Verificar si 'posts' es null y asignar una lista vacía si es necesario
        if (community.getPost() != null) {
            communityResponseDTO.setPost(postMapper.convertToListDTO(community.getPost()));
        } else {
            communityResponseDTO.setPost(new ArrayList<>()); // Si es null, asigna una lista vacía
        }

        communityResponseDTO.setTags(community.getTags());
        return communityResponseDTO;
    }

    public CommunityRequestDTO converToRequestDTO(Community community){
        return modelMapper.map(community, CommunityRequestDTO.class);
    }
    public List<CommunityResponseDTO> convertoToListResponseDTO(List<Community> communities){
        return communities.stream().map(this::converToResponseDTO).toList();
    }
}
