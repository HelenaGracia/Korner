package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificaciones", nullable = false)
    private Integer id;

    @Column(name = "mensaje", length = 256)
    private String mensaje;

    @Column(name = "userTo")
    private String userTo;

    @Column(name = "userFrom")
    private String userFrom;

    @Column(name = "estado")
    private String estado;

    @Column(name = "rutaImagen_From")
    private String rutaImagenUserFrom;

    @Column(name = "tipoElemento")
    private String tipoElemento;

    @Column(name = "id_tipoElemento")
    private Integer idTipoElemento;

    @Column(name = "estadoUsuario")
    private String estadoUsuario;

    @Column(name = "id_userFrom")
    private Integer userFromId;



}
