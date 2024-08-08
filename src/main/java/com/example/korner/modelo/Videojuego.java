package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videojuegos",uniqueConstraints = @UniqueConstraint(columnNames = "titulo"),
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "puntuacion")})

public class Videojuego extends AbstractEntity{

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "videojuego_genero", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_generos_elemt_comp"))
    @NotEmpty
    private Set<GeneroElementoCompartido> generosVideojuegos;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "videojuego_plataforma", joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_plataforma_videojuego"))
    @NotEmpty
    private Set<PlataformaVideojuego> plataformasVideojuego;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "id_videojuego_usuarios")
    private Usuario usuarioVideojuego;

    @Override
    public String toString() {
        return "Videojuego{" +
                "generosVideojuegos=" + generosVideojuegos +
                ", plataformasVideojuego=" + plataformasVideojuego +
                ", usuarioVideojuego=" + usuarioVideojuego +
                '}' + super.toString();
    }
}
