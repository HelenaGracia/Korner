package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String titulo;

    @Column (name = "year")
    @NotNull
    private Short year;

    @Column (name = "puntuacion")
    @Min(0)
    @Max(5)
    private Short puntuacion;

    @Column (name = "opinion" , length = 4000)
    @NotBlank
    private String opinion;

    @Column (name = "imagen" , length = 4000)

    private String imagenRuta;



}
