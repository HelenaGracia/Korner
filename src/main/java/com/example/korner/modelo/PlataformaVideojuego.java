package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * Esta clase representa una plataforma de videojuegos (PS4, Xbox..) que se guarda en la aplicaión y que el usuario
 * usará para indicar en que plataforma ha jugado el videojuego que quiere guardar en la aplicación.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo nombre.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plataformas_videojuegos",uniqueConstraints = @UniqueConstraint(columnNames = {"nombre_plataforma_videojuego"}))
public class PlataformaVideojuego {

    /**
     * id Integer autogenerado de tipo IDENTITY, PK de la tabla plataformas_videojuegos
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plataforma_videojuego", nullable = false)
    private Integer id;

    /**
     * nombre es el String que da nombre a la plataforma, debe tener mínimo 2 caracteres y máximo 30 caracteres
     */
    @Column (name = "nombre_plataforma_videojuego" , length = 30)
    @Size(min = 2, message = "Debe tener como mínimo 2 caracter")
    @Size(max = 30,  message = "Debe tener como máximo 30 caracteres")
    private String nombre;



}
