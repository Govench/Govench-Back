package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.UserCommunity;
import com.upao.govench.govench.model.entity.IdCompuestoU_C;
import com.upao.govench.govench.repository.UserCommunityRepository;
import com.upao.govench.govench.service.UserCommunityService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCommunityServiceImpl implements  UserCommunityService {
    @Autowired
    private UserCommunityRepository UserCommunityRepository;

    @Override
    public UserCommunity addUserCommunity(UserCommunity userCommunity) {
        return UserCommunityRepository.save(userCommunity);
    }

    @Override
    public UserCommunity searchUserCommunityById(IdCompuestoU_C id) {
       return UserCommunityRepository.findById(id).orElse(null);
    }

    @Override
    public void removeUserCommunityById(IdCompuestoU_C id) {
        if (UserCommunityRepository.existsById(id))
        {
            UserCommunityRepository.deleteById(id);
        }
    }

    @Override
    public List<UserCommunity> getAllUserCommunities() {
        return UserCommunityRepository.findAll();
    }

}
