package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "peliculas" ,uniqueConstraints = @UniqueConstraint(columnNames = "titulo"),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year")})
public class Pelicula  extends AbstractEntity{
    @Column (name = "trailer" , length = 1000)
    @NotBlank
    private String trailerRuta;


    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "pelicula_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma"))
    //    @NotEmpty
    private Set<Plataforma> plataformasPelicula;



    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "pelicula_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    //    @NotEmpty
    private Set<GeneroElementoCompartido> generosElementoCompartidoPeliculas;


    @Override
    public String toString() {
        return "Pelicula{" +
                "trailerRuta='" + trailerRuta + '\'' +
                ", plataformasPelicula=" + plataformasPelicula +
                ", generoElementoCompartidoPelicula=" + generosElementoCompartidoPeliculas +
                '}'+ super.toString();
    }
}
