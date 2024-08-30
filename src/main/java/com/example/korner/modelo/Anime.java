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
 * de los suyos propios. Representa el anime que el usuario ha visto y quiere guardar en la aplicación con las validadciones correspondientes.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo título y perteneciente al mismo usuario.
 * Se utiliza los Index para poder optimizar la búsqueda por los campos titulo, year y puntuacion en la BBDD
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "animes",uniqueConstraints = @UniqueConstraint(columnNames = {"titulo","id_anime_usuarios"}),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})
public class Anime extends AbstractEntity{

    /**
     * generosAnime es el conjunto de géneros que tiene el anime
     */
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "anime_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_anime"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosAnime;

    /**
     * plataformasAnime es el conjunto de plataformas que tiene el anime
     */
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "anime_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma_anime"))
    @NotEmpty
    private Set<Plataforma> plataformasAnime;

    /**
     * usuarioAnime es el Usuario al que pertenece el anime
     */
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_anime_usuarios")
    private Usuario usuarioAnime;

    /**
     * animesCompartidas es el conjunto de ElementosCompartidos donde está el anime
     */

    @OneToMany (mappedBy = "anime", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ElementoCompartido> animesCompartidos;


}
