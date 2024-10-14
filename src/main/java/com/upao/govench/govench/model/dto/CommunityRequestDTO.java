package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityRequestDTO {

    private User owner;

    @NotNull(message = "El nombre no puede ser vacio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String name;

    private String descripcion;

    private List<String> tags;
}

