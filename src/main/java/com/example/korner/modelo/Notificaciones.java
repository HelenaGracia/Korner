package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notificaciones")
public class Notificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificaciones", nullable = false)
    private Integer id;

    @Column(name = "descripcion", length = 256)
    private String descripcion;

    @Column(name = "fecha_notificacion")
    private LocalDateTime fechaNotificacion;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", foreignKey = @ForeignKey(name = "fk_usuario_notificaciones"))
    private Usuario usuariosNotificaciones;

}
