package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.CollectionRequestDTO;
import com.upao.govench.govench.model.dto.CollectionResponseDTO;
import com.upao.govench.govench.model.entity.User;
import com.upao.govench.govench.repository.UserRepository;
import com.upao.govench.govench.security.TokenProvider;
import com.upao.govench.govench.service.CollectionService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/user")
    public ResponseEntity<List<CollectionResponseDTO>> getUserCollections() {
        List<CollectionResponseDTO> collections = collectionService.getCollectionsByUserId();
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionResponseDTO> getCollection(@PathVariable Integer collectionId) {
        CollectionResponseDTO collection = collectionService.getCollectionById(collectionId);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CollectionResponseDTO> createCollection(@RequestBody CollectionRequestDTO collectionRequest) {
        CollectionResponseDTO collection = collectionService.createCollection(collectionRequest);
        return new ResponseEntity<>(collection, HttpStatus.CREATED);
    }

    @PutMapping("/{collectionId}")
    public ResponseEntity<CollectionResponseDTO> updateCollection(@PathVariable Integer collectionId, @RequestBody CollectionRequestDTO collection) {
        CollectionResponseDTO collectionResponse = collectionService.updateCollection(collectionId, collection);
        return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<CollectionResponseDTO> deleteCollection(@PathVariable Integer collectionId) {
        collectionService.deleteCollection(collectionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

