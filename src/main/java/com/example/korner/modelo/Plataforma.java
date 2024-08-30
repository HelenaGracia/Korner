package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * Esta clase representa una plataforma (Amazon, HBO..) que se guarda en la aplicaión y que el usuario
 * usará para indicar en que plataforma ha visto la película, serie, videojuego o anime que quiere guardar en la aplicación.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo nombre.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plataformas", uniqueConstraints =@UniqueConstraint(columnNames = "nombre_plataforma"))

public class Plataforma {

    /**
     * id Integer autogenerado de tipo IDENTITY, PK de la tabla plataformas
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plataforma", nullable = false)
    private Integer id;

    /**
     * nombrePlataforma es el String que da nombre a la plataforma, debe tener mínimo 2 caracteres y máximo 30 caracteres
     */
    @Column (name = "nombre_plataforma" , length = 30)
    @Size(min = 2, message = "Debe tener como mínimo 2 caracter")
    @Size(max = 30,  message = "Debe tener como máximo 30 caracteres")
    private String nombrePlataforma;



}
