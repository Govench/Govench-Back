package com.upao.govench.govench.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostDTO {
    private int id;
    private String body;
    private String autorName;
    private String comunidadName;
    private Date created;
}



