package com.upao.govench.govench.repository;

import com.upao.govench.govench.model.entity.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileMongoRepository extends MongoRepository<Profile, String > {
}