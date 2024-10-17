package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.OrganizerDTO;

import java.util.List;

public interface AdminOrganizerService {
    List<OrganizerDTO> getAll();
    OrganizerDTO findById(Integer id);
    OrganizerDTO save(OrganizerDTO organizerDTO);
    OrganizerDTO update(Integer id ,OrganizerDTO organizerDTO);
    void delete(Integer id);

}
