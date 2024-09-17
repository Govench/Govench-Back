package com.upao.govench.govench.service;

import com.upao.govench.govench.model.entity.Profile;
import com.upao.govench.govench.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public interface ProfileService {
    public Profile saveProfileWithImage(MultipartFile file) throws IOException;
    public Profile getProfile(String profileId);
    public User getUserbyId(int userId);
    public void deleteProfile(String profileId);
    public  void validateImageSize(MultipartFile file) throws IOException;
    byte[] resizeImage(MultipartFile file) throws IOException;
}
