package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Profile;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.ProfileMongoRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileMongoRepository profileMongoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    //Para hacer pruebas de subir imagenes
    public Profile saveProfileWithImage(MultipartFile file) throws IOException {
        Profile profile = new Profile();
        profile.setImage(file.getBytes());
        return profileMongoRepository.save(profile);
    }


    @Override
    public User getUserbyId(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void deleteProfile(String profileId) {
        profileMongoRepository.deleteById(profileId);
    }

    public Profile getProfile(String  profileId) {
        return profileMongoRepository.findById(profileId).orElse(null);
    }

}