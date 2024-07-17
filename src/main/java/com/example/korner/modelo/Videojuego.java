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
@Table(name = "videojuegos",
        indexes = {@Index(name = "indice1",columnList = "titulo"),
                @Index(name = "indice2",columnList = "year"),
                @Index(name = "indice3",columnList = "id_generos_elemt_comp")})
public class Videojuego extends AbstractEntity{

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_generos_elemt_comp", foreignKey = @ForeignKey(name = "fk_generos_elemt_comp_videojuegos"))
    private GeneroElementoCompartido generoElementoCompartidoVideojuegos;


    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plataformas", foreignKey = @ForeignKey(name = "fk_plataformas_videojuegos"))
    private Plataforma plataformasVideojuegos;

}
