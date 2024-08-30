package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Esta clase representa una relación de amistad entre dos usuarios de la aplicación. Esta relación incluye atributos
 * como si la relación está bloqueada o pendiente y también contiene una referencia a posibles elementos compartidos entre los amigos.
 * Utiliza UniqueConstraint para que no pueda existir dos registros en la tabla de la BBDD con el mismo usuarioOrigen y el mismo usuarioDestino.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "amigos", uniqueConstraints = {
        @UniqueConstraint(columnNames={"id_amigo_origen", "id_amigo_destino"})})
public class Amigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_amigos", nullable = false)
    private Integer id;

    @Column (name = "bloqueados")
    private Boolean bloqueado;

    @Column (name = "pendientes")
    private Boolean pendiente;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_amigo_origen",foreignKey = @ForeignKey(name = "fk_amigo_origen"))
    private Usuario usuarioOrigen;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_amigo_destino",foreignKey = @ForeignKey(name = "fk_amigo_destino"))
    private Usuario usuarioDestino;

    @OneToMany (mappedBy = "amigos", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ElementoCompartido> elementoCompartidos;


}
