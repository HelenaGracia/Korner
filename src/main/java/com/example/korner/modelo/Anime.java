package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


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


    @Column (name = "opening" , length = 1000)
    private String opening;

    @Column (name = "ending" , length = 1000)
    private String ending;


    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "anime_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_anime"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosAnime;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "anime_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma_anime"))
    @NotEmpty
    private Set<Plataforma> plataformasAnime;


    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_anime_usuarios")
    private Usuario usuarioAnime;

    @OneToMany (mappedBy = "anime", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ElementoCompartido> animesCompartidos;


}
