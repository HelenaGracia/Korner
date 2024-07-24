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
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "id_generos_elemt_comp")})
public class Pelicula  extends AbstractEntity{
    @Column (name = "trailer" , length = 1000)
    @NotBlank
    private String trailerRuta;


    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plataformas", foreignKey = @ForeignKey(name = "fk_plataformas_peliculas"))
    private Plataforma plataformasPelicula;

    @NotEmpty
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "genero_pelicula", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
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
