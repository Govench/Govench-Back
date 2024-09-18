package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CommunityRepository;
import com.upao.govench.govench.model.entity.Post;
import java.util.List;

public interface CommunityService  {
    public Community findById(int id);
    public List<Community> findByOwner_Id(int id);
    List<Community> findByOwner_IdNot(int id);
    public Community save(Community community, User owner);
    public void deleteById(int id);
    public Community update(Community community, int id);


}
