package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CommunityResponseDTO {
    private int id;
    private String name;
    private String descripcion;
    private OwnerResponseDTO owner;
    private List<String> tags;
    private List<PostResponseDTO> post = new ArrayList<>(); // Inicialización con lista vacía
}


