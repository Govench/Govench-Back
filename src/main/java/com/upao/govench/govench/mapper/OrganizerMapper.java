package com.upao.govench.govench.mapper;


import com.upao.govench.govench.model.dto.OrganizerDTO;
import com.upao.govench.govench.model.entity.Organizer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class OrganizerMapper {
    @Autowired
    private ModelMapper modelMapper;

    public OrganizerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public OrganizerDTO toDto(Organizer author) {
        return modelMapper.map(author, OrganizerDTO.class);
    }

    public Organizer toEntity(OrganizerDTO authorDTO) {
        return modelMapper.map(authorDTO, Organizer.class);
    }

}
