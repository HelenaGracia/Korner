package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Esta clase representa un género de una persona (hombre, mujer..) que se guarda en la aplicaión y que el usuario
 * usará para indicar cual es el suyo a la hora de crear una cuenta en la aplicación.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo nombre.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "generos",uniqueConstraints = @UniqueConstraint(columnNames = "descripcion"))
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_generos", nullable = false)
    private Integer id;

    @Size(min = 1, message = "Debe tener como mínimo 1 caracter")
    @Size(max = 30,  message = "Debe tener como máximo 30 caracteres")
    @Column (name = "descripcion" , length = 30)
    private String descripcion;

    @OneToMany (mappedBy = "generos", fetch = FetchType.EAGER)
    private Set<Usuario> usuario;
}
