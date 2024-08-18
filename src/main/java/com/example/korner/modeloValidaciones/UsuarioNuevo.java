package com.example.korner.modeloValidaciones;

import com.example.korner.modelo.Genero;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioNuevo {

    @Size(max = 20,  message = "Debe tener como máximo 20 caracteres")
    @Size(min = 2,  message = "Debe tener como mínimo 2 caracteres")
    @NotBlank
    @Pattern(regexp = "^\\S+$",message = "No puede haber espacios en el nombre")
    private String nombre;

    @NotBlank
    @Size(max = 20,  message = "Debe tener como máximo 20 caracteres")
    @Size(min = 6,  message = "Debe tener como mínimo 6 caracteres")
    private String contrasena;

    @NotBlank
    @Email(message = "Introduzca un Email válido")
    private String correo;

    @Min(1900)
    @Max(2200)
    @NotNull
    private Integer anioNacimiento;

    @NotNull(message = "Debe seleccionar una opción")
    private Genero generos;

}
