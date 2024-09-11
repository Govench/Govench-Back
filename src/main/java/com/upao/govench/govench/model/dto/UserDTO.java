package com.upao.govench.govench.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Date birthday;
    private String gender;
    private String profileDesc;
    private List<String> interest;
    private List<String> skills;
    private List<String> socialLinks;
    private List<String> followers;
    private List<String> followed;
}