package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Esta clase representa un formato de libro (papel, pdf..) que se guarda en la aplicaión y que el usuario
 * usará para indicar de que tipo era el libro que ha leído y quiere guardar en la aplicación
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo nombre.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "formato_libros",uniqueConstraints =@UniqueConstraint(columnNames = "nombre_formato_libro"))
public class FormatoLibro  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formato", nullable = false)
    private Integer id;

    @Column (name = "nombre_formato_libro" , length = 30)
    @Size(min = 2, message = "Debe tener como mínimo 2 caracter")
    @Size(max = 30,  message = "Debe tener como máximo 30 caracteres")
    private String nombre;



}
