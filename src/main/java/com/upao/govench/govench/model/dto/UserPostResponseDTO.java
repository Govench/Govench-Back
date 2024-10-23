// UserPostResponseDTO.java
package com.upao.govench.govench.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPostResponseDTO {
    private int id;
    private String name;
    private String email;
}
