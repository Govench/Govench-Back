package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.CommunityRequestDTO;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import java.util.List;

public interface CommunityService  {
    public CommunityResponseDTO findById(int id);
    public List<CommunityResponseDTO> findByOwner_Id(int id);
    public List<CommunityResponseDTO> getall();
    public List<CommunityResponseDTO> findByOwner_IdNot(int id);
    public CommunityResponseDTO save(CommunityRequestDTO community, User owner);
    public void deleteById(int id);
    public CommunityResponseDTO update(CommunityRequestDTO community, int id);
    public Community EntityfindById(int idcommunity);


}
