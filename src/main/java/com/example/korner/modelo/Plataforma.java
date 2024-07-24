package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "plataformas", uniqueConstraints = @UniqueConstraint(columnNames = "nombre_plataforma"))
public class Plataforma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plataforma", nullable = false)
    private Integer id;


    @Column (name = "nombre_plataforma" , length = 45)
    @NotBlank
    private String nombrePlataforma;

//    @OneToMany (mappedBy = "plataformas_animes", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<Animes> anime;
//
//    @OneToMany (mappedBy = "plataformas_libro", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<Libro> libro;
//
//
//    @OneToMany (mappedBy = "plataformas_series", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<Series> series;
//
//    @OneToMany (mappedBy = "plataformas_videojuegos", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<Videojuego> videojuegos;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipos_elementos", foreignKey = @ForeignKey(name = "fk_tipos_elementos_plataforma"))
    private TipoElemento tipoElemento;
}
