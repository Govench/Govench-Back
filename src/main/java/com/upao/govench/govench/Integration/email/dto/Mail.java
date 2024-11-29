package com.upao.govench.govench.Integration.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    private String from;
    private String to;
    private String subject;
    private Map<String, Object> model;
}
