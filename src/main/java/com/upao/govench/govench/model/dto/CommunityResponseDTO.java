package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Post;
import com.upao.govench.govench.model.entity.User;
import lombok.Data;

import java.util.List;
@Data
public class CommunityResponseDTO {

    private String name;
    private String descripcion;
    private OwnerResponseDTO owner;
    private List<String> tags;
    //private List<Post> post;
}


