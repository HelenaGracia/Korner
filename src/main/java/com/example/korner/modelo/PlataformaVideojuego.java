package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.web.SortDefault;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "plataformas_videojuegos",uniqueConstraints = @UniqueConstraint(columnNames = {"nombre_plataforma_videojuego"}))
public class PlataformaVideojuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plataforma_videojuego", nullable = false)
    private Integer id;

    @Column (name = "nombre_plataforma_videojuego" , length = 30)
    @Size(min = 2, message = "Debe tener como mínimo 2 caracter")
    @Size(max = 30,  message = "Debe tener como máximo 30 caracteres")
    private String nombre;



}
