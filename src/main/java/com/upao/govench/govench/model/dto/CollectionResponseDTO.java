package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectionResponseDTO {

    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userName;
}
