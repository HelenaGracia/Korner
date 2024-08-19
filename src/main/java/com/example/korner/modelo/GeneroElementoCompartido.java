package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Column (name = "nombre_genero" , length = 30)
    @Size(min = 2, message = "Debe tener como mínimo 2 caracter")
    @Size(max = 30,  message = "Debe tener como máximo 30 caracteres")
    private String nombre;




}
