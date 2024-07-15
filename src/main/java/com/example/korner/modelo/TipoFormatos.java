package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_formatos")
public class TipoFormatos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_formatos", nullable = false)
    private Integer id;

    @Column (name = "descripcion" , length = 45)
    private String descripcion;

    @OneToMany (mappedBy = "tipoFormatos", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Libro> libro;

}
