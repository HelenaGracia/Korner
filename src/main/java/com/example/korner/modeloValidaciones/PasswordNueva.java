package com.example.korner.modeloValidaciones;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordNueva {

    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordActual;

    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordNueva;

    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordNueva2;
}
