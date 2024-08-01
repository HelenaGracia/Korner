package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;



@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor


public abstract class AbstractEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column (name = "titulo" , length = 60, nullable = false)
    @Size(min = 1, message = "Debe tener como mínimo 1 caracter")
    @Size(max = 60,  message = "Debe tener como máximo 60 caracteres")
    private String titulo;

    @Column (name = "year")
    @NotNull
    private Short year;

    @Column (name = "puntuacion")
    @Min(0)
    @Max(5)
    @NotNull
    private Short puntuacion;

    @Column (name = "opinion" , length = 4000)
    @Size(max = 4000, message = "Debe tener como máximo 4000 caracteres" )
    private String opinion;

    @Column (name = "imagen" , length = 4000)
    private String imagenRuta;



}
