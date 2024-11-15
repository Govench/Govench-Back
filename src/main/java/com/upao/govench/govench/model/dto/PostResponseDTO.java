package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.Community;
import com.upao.govench.govench.model.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLSession;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private int id;
    private String body;
    private UserBasicDTO autor;
    private int comunidadId; // Agregar el ID de la comunidad
    private LocalDate created;
    private LocalDateTime updated;

}

