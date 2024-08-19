package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "elementos_compartidos")
public class ElementoCompartido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_elemt_compart", nullable = false)
    private Integer id;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_amigos", foreignKey = @ForeignKey(name = "fk_amigos_elemt_comp"))
    private Amigo amigos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_peliculas", foreignKey = @ForeignKey(name = "fk_peliculas_elemt_comp"))
    private Pelicula pelicula;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_series", foreignKey = @ForeignKey(name = "fk_series_elemt_comp"))
    private Serie serie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_animes", foreignKey = @ForeignKey(name = "fk_animes_elemt_comp"))
    private Anime anime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_libros", foreignKey = @ForeignKey(name = "fk_libros_elemt_comp"))
    private Libro libro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_videojuegos", foreignKey = @ForeignKey(name = "fk_videojuegos_elemt_comp"))
    private Videojuego videojuego;


}
