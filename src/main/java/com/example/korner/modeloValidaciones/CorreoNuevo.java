package com.example.korner.modeloValidaciones;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CorreoNuevo {

    @NotBlank
    @Email(message = "Introduzca un Email válido")
    private String correoNuevo;

    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordActual;
}
