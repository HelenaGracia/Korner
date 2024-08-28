package com.example.korner.modeloValidaciones;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * CorreoNuevo es la clase utilizada para cambiar el correo del usuario por uno nuevo
 * con las validaciones correspondientes
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CorreoNuevo {

    /**
     * correoNuevo es el String donde el usuario escribirá su nuevo correo, no puede estar vacío y debe tener
     * un formato de Email valido, por ejemplo "nombre.apellido@example.com"
     */

    @NotBlank
    @Email(message = "Introduzca un Email válido")
    private String correoNuevo;

    /**
     * passwordActual es un String que proporcionará el usuario para verificar que es el dueño de la cuenta y
     * poder cambiar el correo. Debe tener mínimo 6 caracteres y máximo 20 caracteres.
     */
    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordActual;
}
