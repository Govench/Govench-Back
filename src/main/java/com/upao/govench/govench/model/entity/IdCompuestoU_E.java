package com.upao.govench.govench.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class IdCompuestoU_E implements Serializable {
    private int use_id_in;
    private int eve_id_in;
}
