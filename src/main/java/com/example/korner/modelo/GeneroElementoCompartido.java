package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "generos_elementos_compartidos", uniqueConstraints = @UniqueConstraint(columnNames = "nombre_genero"))

public class GeneroElementoCompartido implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genero_elemt_compart", nullable = false)
    private Integer id;

    @Column (name = "nombre_genero" , length = 45)
    private String nombre;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_elemento", foreignKey = @ForeignKey(name = "fk_tipo_elemento_genero_elem_comp"))
    private TipoElemento tipoElemento;

    @Override
    public String toString() {
        return "GeneroElementoCompartido{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipoElemento=" + tipoElemento +
                '}';
    }
}
