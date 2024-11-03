package com.upao.govench.govench.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastname;

    private LocalDate birthday;

    @NotBlank(message = "Ingrese su genero")
    private String gender;

    private String profileDesc;

    @Email(message = "Debe ser una dirección de correo electrónico con formato correcto")
    private String email;

    @NotBlank(message = "La contraseña no puede ir vacia")
    @Size(min = 6 ,message = "La contraseña debe tener minimo 6 caracteres")
    private String password;

    private List<String> interest;

    private List<String> skills;

    private List<String> socialLinks;

    @JsonIgnore
    //Al crear la cuenta sera 0 , e ira subiendo conforme cree mas eventos, solo para organizadores
    private int eventosCreados;
}
