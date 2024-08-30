package com.example.korner.modeloValidaciones;

import com.example.korner.modelo.Genero;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * UsuarioNuevo es la clase utilizada para crear un usuario nuevo con las validaciones correspondientes y donde sus
 * campos luego serán pasados al modelo Usuario
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioNuevo {

    /**
     * nombre es el String donde el usuario escribirá su nombre, no puede estar vacío y debe tener un formato de
     * mínimo 2 caracteres y máximo 20 caracteres, además no debe haber espacios entre caracteres
     */

    @Size(max = 20,  message = "Debe tener como máximo 20 caracteres")
    @Size(min = 2,  message = "Debe tener como mínimo 2 caracteres")
    @NotBlank
    @Pattern(regexp = "^\\S+$",message = "No puede haber espacios en el nombre")
    private String nombre;


    /**
     * contrasena es un String que proporcionará el usuario para asignar a su cuenta una contraseña segura.
     * Debe tener mínimo 6 caracteres y máximo 20 caracteres.
     */

    @NotBlank
    @Size(max = 20,  message = "Debe tener como máximo 20 caracteres")
    @Size(min = 6,  message = "Debe tener como mínimo 6 caracteres")
    private String contrasena;

    /**
     * correo es el String donde el usuario escribirá su correo, no puede estar vacío y debe tener un formato
     * de Email valido, por ejemplo "nombre.apellido@example.com"
     */
    @NotBlank
    @Email(regexp = "^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$" , message = "Introduzca un Email válido")
    private String correo;

    /**
     * anioNacimiento es un Integer que el usuario proporcionará donde nos indicará en que año nació. Estará comprendido
     * entre 1900 como mín y el año 2200 como máximo.
     */
    @Min(1900)
    @Max(2200)
    @NotNull
    private Integer anioNacimiento;

    /**
     * generos es un Genero que nos proporciona el usuario donde nos indicará su género.
     */

    @NotNull(message = "Debe seleccionar una opción")
    private Genero generos;

}
