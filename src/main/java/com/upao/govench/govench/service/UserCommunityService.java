package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.model.entity.UserCommunity;
import com.upao.govench.govench.model.entity.IdCompuestoU_C;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserCommunityService {


    public UserCommunity addUserCommunity(UserCommunity userCommunity);

    List<UserCommunity> getUserCommunityByUser(User user);

    public UserCommunity searchUserCommunityById(IdCompuestoU_C id);

    public void removeUserCommunityById(IdCompuestoU_C id);

    public List<UserCommunity> getAllUserCommunities();
}
