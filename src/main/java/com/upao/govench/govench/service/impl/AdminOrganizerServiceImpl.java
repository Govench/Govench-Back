package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.exceptions.BadRequestException;
import com.upao.govench.govench.mapper.OrganizerMapper;
import com.upao.govench.govench.model.dto.OrganizerDTO;
import com.upao.govench.govench.model.entity.Organizer;
import com.upao.govench.govench.repository.OrganizerRepository;
import com.upao.govench.govench.service.AdminOrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class AdminOrganizerServiceImpl implements AdminOrganizerService {
    @Autowired
    OrganizerRepository organizerRepository;
    @Autowired
    OrganizerMapper organizerMapper;

    @Transactional(readOnly = true)
    @Override
    public List<OrganizerDTO> getAll() {
        List<Organizer> authors = organizerRepository.findAll();
        return authors.stream()
                .map(organizerMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public OrganizerDTO findById(Integer id) {
       Organizer organizer =  organizerRepository.findById(id) .orElseThrow(() -> new NotFoundException("El organizador con ID " + id + " no fue encontrado"));
        return organizerMapper.toDto(organizer);
    }

    @Override
    public OrganizerDTO save(OrganizerDTO organizerDTO) {
        organizerRepository.findByNameAndLastname(organizerDTO.getName(), organizerDTO.getLastname())
                .ifPresent(existingAuthor -> {
                    throw new BadRequestException("El organizador ya existe con el mismo nombre y apellido");
                });

        Organizer organizer = organizerMapper.toEntity(organizerDTO);
        organizer.setCreated(LocalDateTime.now());
        organizer = organizerRepository.save(organizer);
        return organizerMapper.toDto(organizer);
    }

    @Override
    public OrganizerDTO update(Integer id, OrganizerDTO organizerDTO) {
        Organizer organizerfrombd = organizerRepository.findById(id) .orElseThrow(() -> new NotFoundException("El organizador con ID " + id + " no fue encontrado"));

        organizerRepository.findByNameAndLastname(organizerDTO.getName(), organizerDTO.getLastname())
                .filter(existingAuthor -> !existingAuthor.getId().equals(id))
                .ifPresent(existingAuthor -> {
                    throw new BadRequestException("Ya existe un organizador con el mismo nombre y apellido");
                });

        // Actualizar los campos
        if(organizerDTO.getName()!= null)organizerfrombd.setName(organizerDTO.getName());
        if(organizerDTO.getLastname()!= null)organizerfrombd.setLastname(organizerDTO.getLastname());
        if(organizerDTO.getProfileDesc() != null)organizerfrombd.setProfileDesc(organizerDTO.getProfileDesc());
        if(organizerDTO.getInterest()!=null) organizerfrombd.setInterest(organizerDTO.getInterest());
        if(organizerDTO.getSkills()!=null) organizerDTO.setSkills(organizerDTO.getSkills());
        if(organizerDTO.getSocialLinks()!=null)organizerfrombd.setSocialLinks(organizerDTO.getSocialLinks());
        organizerfrombd.setUpdated(LocalDateTime.now());

        organizerfrombd = organizerRepository.save(organizerfrombd);
        return organizerMapper.toDto(organizerfrombd);
    }

    @Override
    public void delete(Integer id) {
        Organizer organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("El organizador con ID " + id + " no fue encontrado"));
        organizerRepository.delete(organizer);
    }
}
