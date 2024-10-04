package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.model.entity.Profile;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.ProfileMongoRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.service.ProfileService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;

import java.io.IOException;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final int MAX_WIDTH = 600;
    private static final int MAX_HEIGHT = 600;

    @Autowired
    private ProfileMongoRepository profileMongoRepository;

    @Autowired
    private UserRepository userRepository;



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

    @Override
    //Para hacer pruebas de subir imagenes
    public Profile saveProfileWithImage(MultipartFile file) throws IOException {

        byte[] resizedImage = resizeImage(file);

        Profile profile = new Profile();

        profile.setImage(resizedImage);
        return profileMongoRepository.save(profile);
    }


    public void validateImageSize(MultipartFile file) throws IOException {
        if (file.getSize() > MAX_SIZE) {
            throw new IOException("El tamaño del archivo excede el límite permitido de 5 MB.");
        }
    }

    public byte[] resizeImage(MultipartFile file) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .size(MAX_WIDTH, MAX_HEIGHT)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }
}