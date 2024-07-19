package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "animes",
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "id_generos_elemt_comp")})
public class Animes extends AbstractEntity{


    @Column (name = "opening" , length = 1000)
    private String opening;

    @Column (name = "ending" , length = 1000)
    private String ending;


    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_generos_elemt_comp", foreignKey = @ForeignKey(name = "fk_generos_elemt_comp_animes"))
    private GeneroElementoCompartido generoElementoCompartidoAnimes;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plataformas", foreignKey = @ForeignKey(name = "fk_plataformas_animes"))
    private Plataforma plataformasAnimes;


}
