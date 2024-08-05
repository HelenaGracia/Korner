package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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


    @Column (name = "nombre_plataforma" , length = 30)
    @Size(min = 2, message = "Debe tener como mínimo 2 caracter")
    @Size(max = 30,  message = "Debe tener como máximo 30 caracteres")
    private String nombrePlataforma;



    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipos_elementos", foreignKey = @ForeignKey(name = "fk_tipos_elementos_plataforma"))
    private TipoElemento tipoElemento;
}
