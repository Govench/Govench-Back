package com.upao.govench.govench.mapper;

import com.upao.govench.govench.model.dto.CollectionRequestDTO;
import com.upao.govench.govench.model.dto.CollectionResponseDTO;
import com.upao.govench.govench.model.entity.Collection;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollectionMapper {

    private final ModelMapper modelMapper;

    public CollectionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        //Configurar ModelMapper para usar estrategia de conincidencia estricta
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    //Create - Update
    public Collection convertToEntity(CollectionRequestDTO collectionRequestDTO) {
        return modelMapper.map(collectionRequestDTO, Collection.class);
    }

    public CollectionRequestDTO tocollectionRequestDTO(Collection collection) {
        return modelMapper.map(collection, CollectionRequestDTO.class);
    }

    public CollectionResponseDTO convertToDTO(Collection collection) {

        CollectionResponseDTO collectionResponseDTO = modelMapper.map(collection, CollectionResponseDTO.class);

        if (collection.getUser().getParticipant() != null) {
            collectionResponseDTO.setUserName(collection.getUser().getParticipant().getName()+" "+ collection.getUser().getParticipant().getLastname());
        }

        if (collection.getUser().getOrganizer() != null) {
            collectionResponseDTO.setUserName(collection.getUser().getOrganizer().getName()+" "+ collection.getUser().getOrganizer().getLastname());
        }

        if (collection.getUser().getAdmin() != null) {

        }

        return collectionResponseDTO;
    }

    public List<CollectionResponseDTO> convertToListDTO(List<Collection> collections) {
        return collections.stream()
                .map(this::convertToDTO)
                .toList();
    }

}
