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
@Table(name = "libros",
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "id_generos_elemt_comp")})
public class Libro extends AbstractEntity{


    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_generos_elemt_comp", foreignKey = @ForeignKey(name = "fk_generos_elemt_comp_libro"))
    private GeneroElementoCompartido generoElementoCompartidoLibro;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plataformas", foreignKey = @ForeignKey(name = "fk_plataformas_libro"))
    private Plataforma plataformasLibro;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipos_formatos", foreignKey = @ForeignKey(name = "fk_tipos_formatos_libro"))
    private TipoFormatos tipoFormatos;

}
