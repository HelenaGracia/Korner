package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "peliculas" ,uniqueConstraints = @UniqueConstraint(columnNames = {"titulo","id_pelicula_usuarios"}),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})
public class Pelicula  extends AbstractEntity{
    @Column (name = "trailer" , length = 1000)
    private String trailerRuta;


    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "pelicula_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma"))
    @NotEmpty
    private Set<Plataforma> plataformasPelicula;



    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "pelicula_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosPelicula;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_pelicula_usuarios")
    private Usuario usuarioPelicula;

    @OneToMany (mappedBy = "pelicula", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ElementoCompartido> peliculasCompartidas;
}
