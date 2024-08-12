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
@Table(name = "tipo_elementos")
public class TipoElemento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_elementos", nullable = false)
    private Integer id;

    @Column(name = "descripcion", length = 45)
    private String descripcion;

//    @OneToMany (mappedBy = "tipoElemento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<ElementoCompartido> elementoCompartidos;

    @OneToMany (mappedBy = "tipoElemento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<GeneroElementoCompartido> GeneroElementoCompartidos;

    @OneToMany (mappedBy = "tipoElemento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Plataforma> plataformas;

}
