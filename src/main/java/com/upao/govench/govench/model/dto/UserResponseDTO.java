package com.upao.govench.govench.model.dto;

import com.upao.govench.govench.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private LocalDate birthday;
    private String gender;
    private String profileDesc;
    private List<String> interest;
    private List<String> skills;
    private List<String> socialLinks;
    private List<User> followers;
    private List<User> followings;
}