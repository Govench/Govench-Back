package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.repository.PostRepository;
import com.upao.govench.govench.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;


    @Override
    public List<Community> findByOwner_Id(int userId) {
        return communityRepository.findByOwner_Id(userId);
    }

    @Override
    public List<Community> findByOwner_IdNot(int userId) {
        return communityRepository.findByOwner_IdNot(userId);
    }

    @Override
    public Community findById(int id) {
        return communityRepository.findById(id).orElse(null);
    }

    @Override
    public Community save(Community community, User owner) {
        community.setOwner(owner);
        return communityRepository.save(community);
    }

    @Override
    public void deleteById(int id) {
        communityRepository.deleteById(id);
    }

    @Override
    public Community update(Community community, int id) {
       Community community1 =findById(id);
        if(community1 != null){
            if(community.getNombre() != null) community1.setNombre(community.getNombre());
            if(community.getDescripcion()!=null)community1.setDescripcion(community.getDescripcion());
            return communityRepository.save(community1);
        }
        return null;
    }
}
