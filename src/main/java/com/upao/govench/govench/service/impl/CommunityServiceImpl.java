package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.mapper.CommunityMapper;
import com.upao.govench.govench.mapper.UserMapper;
import com.upao.govench.govench.model.dto.CommunityRequestDTO;
import com.upao.govench.govench.model.dto.CommunityResponseDTO;
import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.repository.PostRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<CommunityResponseDTO> findByOwner_Id(int userId) {
         User usuario = userRepository.findById(userId).orElse(null);
       if(usuario != null) {
           return communityMapper.convertoToListResponseDTO(communityRepository.findByOwner_id(userId));
       }
        else
            return null;
    }

    @Override
    public List<CommunityResponseDTO> findByOwner_IdNot(int userId) {
        User usuario = userRepository.findById(userId).orElse(null);
        if(usuario != null) {
            return communityMapper.convertoToListResponseDTO(communityRepository.findByOwner_idNot(userId));

        }
        else {
            return null;
        }
    }

    public CommunityResponseDTO findById(int id) {
        Community community = communityRepository.findById(id).orElse(null);
        if (community == null) {
            throw new NullPointerException("La comunidad con ID " + id + " no existe");
        }
        return communityMapper.converToResponseDTO(community);
    }

    @Override
    public CommunityResponseDTO save(CommunityRequestDTO community, User owner) {

        Community comunityentity = communityMapper.convertToEntity(community);
        comunityentity.setOwner(owner);
        return  communityMapper.converToResponseDTO(communityRepository.save(comunityentity));
    }

    @Override
    public void deleteById(int id) {
        communityRepository.deleteById(id);
    }

    @Override
    public CommunityResponseDTO update(CommunityRequestDTO community, int id) {
        Community community1 = communityRepository.findById(id).orElse(null);
        if(community1 != null){
            if(community.getName() != null) community1.setName(community.getName());
            if(community.getDescripcion()!=null)community1.setDescripcion(community.getDescripcion());
            if(community.getTags()!=null)community1.setTags(community.getTags()); //probar si sobrescribe o agrega
            return communityMapper.converToResponseDTO(communityRepository.save(community1));
        }
        return null;
    }

    @Override
    public Community EntityfindById(int idcommunity) {
        return communityRepository.findById(idcommunity).orElse(null);
    }

    @Override
    public List<CommunityResponseDTO> getall() {

        return communityMapper.convertoToListResponseDTO(communityRepository.findAll());

    }
}
