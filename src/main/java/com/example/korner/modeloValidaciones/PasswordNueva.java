package com.example.korner.modeloValidaciones;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PasswordNueva es la clase utilizada para cambiar la contraseña del usuario por uno nueva
 * con las validaciones correspondientes
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordNueva {

    /**
     * passwordActual es un String que proporcionará el usuario para verificar que es el dueño de la cuenta y
     * poder cambiar la contraseña actual. Debe tener mínimo 6 caracteres y máximo 20 caracteres.
     */

    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordActual;

    /**
     * passwordNueva es un String que proporcionará el usuario con la nueva contraseña que quiere tener.
     * Debe tener mínimo 6 caracteres y máximo 20 caracteres.
     */
    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordNueva;


    /**
     * passwordNueva2 es un String que proporcionará el usuario con la nueva contraseña que quiere tener para verificar
     * que ha puesto la contraseña como realmente quería. Debe ser igual que passwordNueva y debe tener mínimo 6
     * caracteres y máximo 20 caracteres.
     */
    @Size(min = 6,message = "mínimo 6 caracteres")
    @Size(max = 20,message = "máximo 20 caracteres")
    private String passwordNueva2;
}
