package com.upao.govench.govench.service;

import com.upao.govench.govench.model.dto.CollectionRequestDTO;
import com.upao.govench.govench.model.dto.CollectionResponseDTO;

import java.util.List;

public interface CollectionService {

    List<CollectionResponseDTO> getCollectionsByUserId();
    CollectionResponseDTO getCollectionById(Integer id);
    CollectionResponseDTO createCollection(CollectionRequestDTO collection);
    CollectionResponseDTO updateCollection(Integer collectionId, CollectionRequestDTO collection);
    void deleteCollection(Integer collectionId);
}
