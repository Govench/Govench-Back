package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityRepository CommunityRepository;

    @Override
    public List<Community> findByOwner_Id(int userId) {
        return CommunityRepository.findByOwner_Id(userId);
    }

    // Comunidades que no fueron creadas por el usuario
    public List<Community> findByOwner_IdNot(int userId) {
        return CommunityRepository.findByOwner_IdNot(userId);
    }

    @Override
    public Community findById(int id) {
        return CommunityRepository.findById(id).orElse(null);
    }

    @Override
    public Community save(Community community, User owner) {
        community.setOwner(owner);
        return CommunityRepository.save(community);
    }

    @Override
    public void deleteById(int id) {
        CommunityRepository.deleteById(id);
    }

    @Override
    public Community update(Community community, int id) {
       Community community1 =findById(id);
        if(community1 != null){
            if(community.getNombre() != null) community1.setNombre(community.getNombre());
            if(community.getDescripcion()!=null)community1.setDescripcion(community.getDescripcion());
            return CommunityRepository.save(community1);
        }
        return null;
    }
}
