package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

/**
 * Esta clase extiende de AbstractEntity, lo que significa que hereda las propiedades y comportamientos definidos en esa clase base además
 * de los suyos propios. Representa la película que el usuario ha visto y quiere guardar en la aplicación con las validadciones correspondientes.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo título y perteneciente al mismo usuario.
 * Se utiliza los Index para poder optimizar la búsqueda por los campos titulo, year y puntuacion en la BBDD
 */
@Getter
@Setter
@Entity
@Table(name = "peliculas" ,uniqueConstraints = @UniqueConstraint(columnNames = {"titulo","id_pelicula_usuarios"}),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})
public class Pelicula  extends AbstractEntity{

    /**
     * trailerRuta es el String que nos indicará la ruta del videoWeb que se mostrará en la película
     */
    @Column (name = "trailer" , length = 1000)
    private String trailerRuta;


    /**
     * plataformasPelicula es el conjunto de plataformas que tiene la película
     */

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "pelicula_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma"))
    @NotEmpty
    private Set<Plataforma> plataformasPelicula;

    /**
     * generosPelicula es el conjunto de géneros que tiene la película
     */


    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "pelicula_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosPelicula;

    /**
     * usuarioPelicula es el Usuario al que pertenece la película
     */

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_pelicula_usuarios")
    private Usuario usuarioPelicula;

    /**
     * peliculasCompartidas es el conjunto de ElementosCompartidos donde está la película
     */

    @OneToMany (mappedBy = "pelicula", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ElementoCompartido> peliculasCompartidas;
}
