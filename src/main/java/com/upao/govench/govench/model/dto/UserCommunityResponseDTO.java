package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.IdCompuestoU_C;

import java.time.LocalDate;

public class UserCommunityResponseDTO {
    private OwnerResponseDTO user;
    private CommunityResponseDTO community;
    private LocalDate date;
}
