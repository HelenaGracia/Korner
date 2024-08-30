package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Rol es la Clase que tiene los tipos de roles que tendran los usuarios
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Rol {

    /**
     * id Integer autogenerado de tipo IDENTITY, PK de la tabla roles
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol", nullable = false)
    private Integer id;

    /**
     * nombre es el String que nos indica el nombre del rol
     */

    @Column (name = "nombre_rol" , length = 45)
    private String nombre;


    /**
     * usuario es el conjunto de Usuario al cual pertenecen el rol
     */

    @OneToMany (mappedBy = "role", fetch = FetchType.EAGER)
    private Set<Usuario> usuario;
}
