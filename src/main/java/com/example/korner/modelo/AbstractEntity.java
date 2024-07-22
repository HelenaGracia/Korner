package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

    @Column (name = "titulo" , length = 256, nullable = false)
    @NonNull
    private String titulo;

    @Column (name = "year")
    @NonNull
    private Short year;

    @Column (name = "puntuacion")
    @NonNull
    private Short puntuacion;

    @Column (name = "opinion" , length = 4000)
    @NonNull
    private String opinion;

    @Column (name = "imagen" , length = 4000)
    @NonNull
    private String imagenRuta;



}
