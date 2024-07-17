package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "peliculas" ,
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "id_generos_elemt_comp")})
public class Pelicula  extends AbstractEntity{
    @Column (name = "trailer" , length = 1000)
    @NonNull
    private String trailerRuta;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plataformas", foreignKey = @ForeignKey(name = "fk_plataformas_peliculas"))
    private Plataforma plataformasPelicula;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_generos_elemt_comp", foreignKey = @ForeignKey(name = "fk_generos_elemt_comp_peliculas"))
    private GeneroElementoCompartido generoElementoCompartidoPelicula;


    @Override
    public String toString() {
        return "Pelicula{" +
                "trailerRuta='" + trailerRuta + '\'' +
                ", plataformasPelicula=" + plataformasPelicula +
                ", generoElementoCompartidoPelicula=" + generoElementoCompartidoPelicula +
                '}'+ super.toString();
    }
}
