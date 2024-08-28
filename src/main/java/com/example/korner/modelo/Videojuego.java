package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Esta clase extiende de AbstractEntity, lo que significa que hereda las propiedades y comportamientos definidos en esa clase base además
 * de los suyos propios. Representa el videojuego que el usuario ha visto y quiere guardar en la aplicación con las validadciones correspondientes.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo título y perteneciente al mismo usuario.
 * Se utiliza los Index para poder optimizar la búsqueda por los campos titulo, year y puntuacion en la BBDD
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videojuegos",uniqueConstraints = @UniqueConstraint(columnNames = {"titulo","id_videojuego_usuarios"}),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})

public class Videojuego extends AbstractEntity{

    /**
     * generosVideojuegos es el conjunto de géneros que tiene el videojuego
     */

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "videojuego_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosVideojuegos;

    /**
     * plataformasVideojuego es el conjunto de plataformas que tiene el videojuego
     */

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "videojuego_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma_videojuego"))
    @NotEmpty
    private Set<PlataformaVideojuego> plataformasVideojuego;

    /**
     * usuarioVideojuego es el Usuario al que pertenece el videojuego
     */

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_videojuego_usuarios")
    private Usuario usuarioVideojuego;

    /**
     * videojuegosCompartidas es el conjunto de ElementosCompartidos donde está el videojuego
     */

    @OneToMany (mappedBy = "videojuego", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ElementoCompartido> videojuegosCompartidos;
}
