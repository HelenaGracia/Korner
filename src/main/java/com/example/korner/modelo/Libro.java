package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "libros" ,uniqueConstraints = @UniqueConstraint(columnNames = {"titulo","id_libro_usuarios"}),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})
public class Libro  extends AbstractEntity{

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "libro_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma"))
    @NotEmpty
    private Set<Plataforma> plataformasLibro;



    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "libro_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosLibro;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_libro_usuarios")
    private Usuario usuarioLibro;
    
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_tipo_formatos")
    private TipoFormatos tipoFormatos;

    @Override
    public String toString() {
        return "Libro{" +
                ", plataformasLibro=" + plataformasLibro +
                ", generoElementoCompartidoLibro=" + generosLibro +
                '}'+ super.toString();
    }
}
