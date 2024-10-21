package com.upao.govench.govench.service.impl;

import com.upao.govench.govench.mapper.CollectionMapper;
import com.upao.govench.govench.model.dto.CollectionRequestDTO;
import com.upao.govench.govench.model.dto.CollectionResponseDTO;
import com.upao.govench.govench.model.entity.Collection;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.CollectionRepository;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.security.TokenProvider;
import com.upao.govench.govench.service.CollectionService;
import com.upao.govench.govench.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CollectionMapper collectionMapper;

    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    @Override
    public List<CollectionResponseDTO> getCollectionsByUserId() {
        Integer userId = userService.getAuthenticatedUserIdFromJWT();
        List<Collection> collections = collectionRepository.findByUserId(userId);
        return collectionMapper.convertToListDTO(collections);
    }

    @Transactional(readOnly = true)
    @Override
    public CollectionResponseDTO getCollectionById(Integer id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        return collectionMapper.convertToDTO(collection);
    }

    @Transactional
    @Override
    public CollectionResponseDTO createCollection(CollectionRequestDTO collectionRequestDTO) {

        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Collection collection = collectionMapper.convertToEntity(collectionRequestDTO);

        collection.setUser(user);

        collection.setCreatedAt(LocalDateTime.now());

        collectionRepository.save(collection);
        return collectionMapper.convertToDTO(collection);
    }

    @Transactional
    @Override
    public CollectionResponseDTO updateCollection(Integer collectionId, CollectionRequestDTO collectionRequestDTO) {

        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        if (collection.getUser().getId() != userId){
            throw new RuntimeException("No puedes editar la colección, No eres Dueño");
        }
        if (collectionRequestDTO.getName()!=null) {collection.setName(collectionRequestDTO.getName());}
        collection.setUpdatedAt(LocalDateTime.now());
        collectionRepository.save(collection);
        return collectionMapper.convertToDTO(collection);

    }

    @Transactional
    @Override
    public void deleteCollection(Integer collectionId) {

        Integer userId = userService.getAuthenticatedUserIdFromJWT();

        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        if (collection.getUser().getId() != userId){
            throw new RuntimeException("No puedes Eliminar la colección, No eres Dueño");
        }

        collectionRepository.delete(collection);
    }

}
