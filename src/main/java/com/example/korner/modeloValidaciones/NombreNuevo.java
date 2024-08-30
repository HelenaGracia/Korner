package com.example.korner.modeloValidaciones;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * NombreNuevo es la clase utilizada para cambiar el nombre del usuario por uno nuevo
 * con las validaciones correspondientes
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NombreNuevo {


    /**
     * nombre es el String donde el usuario escribirá su nuevo nombre, no puede estar
     * vacío y debe tener un formato de mínimo 2 caracteres y máximo 20 caracteres, además
     * no debe haber espacios entre caracteres
     */

    @Size(max = 20,  message = "Debe tener como máximo 20 caracteres")
    @Size(min = 2,  message = "Debe tener como mínimo 2 caracteres")
    @NotBlank
    @Pattern(regexp = "^\\S+$",message = "No puede haber espacios en el nombre")
    private String nombre;


    /**
     * passwordActual es un String que proporcionará el usuario para verificar que es el dueño de la cuenta y
     * poder cambiar el nombre. Debe tener mínimo 6 caracteres y máximo 20 caracteres.
     */

    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordActual;
}
