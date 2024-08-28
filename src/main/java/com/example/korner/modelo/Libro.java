package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;
/**
 * Esta clase extiende de AbstractEntity, lo que significa que hereda las propiedades y comportamientos definidos en esa clase base además
 * de los suyos propios. Representa el libro que el usuario ha leído y quiere guardar en la aplicación con las validadciones correspondientes.
 *  * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo título y perteneciente al mismo usuario.
 *  * Se utiliza los Index para poder optimizar la búsqueda por los campos titulo, year y puntuacion en la BBDD
 */
@Getter
@Setter
@Entity
@Table(name = "libros" ,uniqueConstraints = @UniqueConstraint(columnNames = {"titulo","id_libro_usuarios"}),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})
public class Libro  extends AbstractEntity{

    /**
     * formatosLibros es el conjunto de formatos que tiene el libro
     */
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "libro_formato", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_formato"))
    @NotEmpty
    private Set<FormatoLibro> formatosLibro;

    /**
     * generosLibro es el conjunto de géneros que tiene el libro
     */

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "libro_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosLibro;

    /**
     * usuarioLibro es el Usuario al que pertenece el libro
     */
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_libro_usuarios")
    private Usuario usuarioLibro;

    /**
     * librosCompartidas es el conjunto de ElementosCompartidos donde está el libro
     */

    @OneToMany (mappedBy = "libro", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ElementoCompartido> librosCompartidos;

}
