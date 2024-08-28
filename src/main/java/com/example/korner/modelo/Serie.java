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
 * de los suyos propios. Representa la serie que el usuario ha visto y quiere guardar en la aplicación con las validadciones correspondientes.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo título y perteneciente al mismo usuario.
 * Se utiliza los Index para poder optimizar la búsqueda por los campos titulo, year y puntuacion en la BBDD
 */
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


    /**
     * trailerRuta es el String que nos indicará la ruta del videoWeb que se mostrará en la serie
     */

    @Column (name = "trailer" , length = 1000)
    private String trailerRuta;

    /**
     * plataformasSerie es el conjunto de plataformas que tiene la serie
     */

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "serie_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma"))
    @NotEmpty
    private Set<Plataforma> plataformasSerie;

    /**
     * generosSerie es el conjunto de géneros que tiene la serie
     */

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "serie_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosSerie;

    /**
     * usuarioSerie es el Usuario al que pertenece la serie
     */

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_serie_usuarios")
    private Usuario usuarioSerie;

    /**
     * seriesCompartidas es el conjunto de ElementosCompartidos donde está la serie
     */
    @OneToMany (mappedBy = "serie", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ElementoCompartido> seriesCompartidas;
}
