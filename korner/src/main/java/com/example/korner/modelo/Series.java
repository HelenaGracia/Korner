package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "series",
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "id_generos_elemt_comp")})
public class Series extends AbstractEntity{


    @Column (name = "trailer" , length = 1000)
    private String trailer;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_generos_elemt_comp", foreignKey = @ForeignKey(name = "fk_generos_elemt_comp_series"))
    private GeneroElementoCompartido generoElementoCompartidoSeries;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plataformas", foreignKey = @ForeignKey(name = "fk_plataformas_series"))
    private Plataforma plataformasSeries;
}
