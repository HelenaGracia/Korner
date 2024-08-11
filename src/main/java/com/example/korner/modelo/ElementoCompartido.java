package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "elementos_compartidos", uniqueConstraints = {
        @UniqueConstraint(columnNames={"id_amigos"})})
public class ElementoCompartido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_elemt_compart", nullable = false)
    private Integer id;

//    @Column (name = "fecha_compartir")
//    private LocalDateTime fecha;
//
//    @Column (name = "me_gusta")
//    private Integer like;
//
//    @Column (name = "id_elemento")
//    private Integer idElemento;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_amigos", foreignKey = @ForeignKey(name = "fk_amigos_elemt_comp"))
    private Amigo amigos;

//    @ManyToOne (fetch = FetchType.EAGER)
//    @JoinColumn(name = "id_tipos_elementos", foreignKey = @ForeignKey(name = "fk_tipos_elementos_elemt_comp"))
//    private TipoElemento tipoElemento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_peliculas", foreignKey = @ForeignKey(name = "fk_peliculas_elemt_comp"))
    private Pelicula pelicula;


}
