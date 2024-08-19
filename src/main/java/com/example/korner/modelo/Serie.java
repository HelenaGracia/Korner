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
@Table(name = "series",uniqueConstraints = @UniqueConstraint(columnNames = {"titulo","id_serie_usuarios"}),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})
public class Serie extends AbstractEntity{


    @Column (name = "trailer" , length = 1000)
    private String trailerRuta;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "serie_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma"))
    @NotEmpty
    private Set<Plataforma> plataformasSerie;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "serie_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosSerie;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_serie_usuarios")

    private Usuario usuarioSerie;

}
