package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Notificacion es la clase utilizada para que a los usuarios les llegué las notificaciones cuando interactuan
 * con otros usuarios
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notificaciones")
public class Notificacion {

    /**
     * id Integer autogenerado de tipo IDENTITY, PK de la tabla notificaciones
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificaciones", nullable = false)
    private Integer id;


    /**
     * mensaje es el String que nos indica de que trata la notificación que tenemos
     */

    @Column(name = "mensaje", length = 256)
    private String mensaje;

    /**
     * userTo es el nombre del usuario al que va dirigida la notificación
     */

    @Column(name = "userTo")
    private String userTo;

    /**
     * userFrom es el nombre del usuario del que procede la notificación
     */

    @Column(name = "userFrom")
    private String userFrom;

    /**
     * estado es el campo utilizado para saber si una notificación ha sido leida o no
     */
    @Column(name = "estado")
    private String estado;

    /**
     * rutaImagenUserFrom es el campo utilizado para saber la imagen del usuario del que proviene la notificacion
     * y poder visualizar su imagen junto al mensaje
     */


    @Column(name = "rutaImagen_From")
    private String rutaImagenUserFrom;

    /**
     * tipoElemento es el String que nos indica que tipo de notificación es el que tenemos y se utilizará para controlar
     * si la notificación proviene de un objeto que ya no existe
     */

    @Column(name = "tipoElemento")
    private String tipoElemento;

    /**
     * idTipoElemento es identificador por el cual buscamos el tipo de elemento que nos llega para comprobar si existe
     * o no existe ese objeto.
     */
    @Column(name = "id_tipoElemento")
    private Integer idTipoElemento;

    /**
     * estadoUsuario nos indica si la cuenta del usuario del que proviene la notificación esta activa o no
     */


    @Column(name = "estadoUsuario")
    private String estadoUsuario;

    /**
     * userFromId identificador del usuario del que proviene la notificación
     */


    @Column(name = "id_userFrom")
    private Integer userFromId;



}
