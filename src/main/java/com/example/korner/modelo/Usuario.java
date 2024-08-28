package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.*;


/**
 * Usuario es la clase utilizada para crear un usuario nuevo con las validaciones correspondientes
 * Existe UniqueConstraint para que no pueda haber dos registros en la tabla de la BBDD con el mismo nombre y correo.
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nombre"}),
        @UniqueConstraint(columnNames = "correo")})
public class Usuario implements Serializable, UserDetails {

    /**
     * id Integer autogenerado de tipo AUTO, PK de la tabla usuarios
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    /**
     * nombre es el String donde el usuario escribirá su nombre, no puede estar vacío y debe tener un formato de
     * mínimo 2 caracteres y máximo 20 caracteres, además no debe haber espacios entre caracteres
     */

    @Size(max = 20,  message = "Debe tener como máximo 20 caracteres")
    @Size(min = 2,  message = "Debe tener como mínimo 2 caracteres")
    @NotBlank
    @Pattern(regexp = "^\\S+$",message = "No puede haber espacios en el nombre")
    @Column (name = "nombre" , length = 20)
    private String nombre;

    /**
     * anioNacimiento es un Integer que el usuario proporcionará donde nos indicará en que año nació. Estará comprendido
     * entre 1900 como mín y el año 2200 como máximo.
     */
    @Column (name = "anio_nacimiento")
    @Min(1900)
    @Max(2200)
    @NotNull
    private Integer anioNacimiento;


    /**
     * activa es un Boolean que nos indicará si la cuenta del usuario esta activa o inactiva
     */

    @Column (name = "activa")
    private Boolean activa;

    /**
     * contrasena es un String que proporcionará el usuario para asignar a su cuenta una contraseña segura.
     * Debe tener mínimo 6 caracteres y máximo 20 caracteres.
     */

    @Column (name = "contrasena" , length = 100)
    @NotBlank
    private String contrasena;

    /**
     * correo es el String donde el usuario escribirá su correo, no puede estar vacío y debe tener un formato
     * de Email valido, por ejemplo "nombre.apellido@example.com"
     */

    @Column (name = "correo" , length = 45)
    @NotBlank
    @Email(regexp = "^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$" , message = "Introduzca un Email válido")
    private String correo;

    /**
     * rutaImagen es el String donde guardaremos la ruta completa de la imagen de perfil del usuario
     */

    @Column (name = "imagen")
    private String rutaImagen;

    /**
     * ajustesInicioSesion es un String que nos indicará hacia que endpoint redirigir tras hacer el login
     */

    @Column (name = "ajustes_sesion")
    private String ajustesInicioSesion;

    /**
     * role es el Rol que tiene asignado el usuario
     */

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_roles", foreignKey = @ForeignKey(name = "fk_rol_usuario"))
    private Rol role;

    /**
     * generos es el Genero que tiene asignado el usuario
     */

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_generos", foreignKey = @ForeignKey(name = "fk_genero_usuario"))
    @NotNull(message = "Debe seleccionar una opción")
    private Genero generos;

    /**
     * animes es el conjunto de animes que tiene el usuario
     */

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioAnime")
    private Set<Anime> animes;

    /**
     * libros es el conjunto de libros que tiene el usuario
     */


    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioLibro")
    private Set<Libro> libros;

    /**
     * videojuegos es el conjunto de videojuegos que tiene el usuario
     */


    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioVideojuego")
    private Set<Videojuego> videojuegos;

    /**
     * peliculas es el conjunto de peliculas que tiene el usuario
     */

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioPelicula")
    private Set<Pelicula> peliculas;


    /**
     * series es el conjunto de series que tiene el usuario
     */

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioSerie")
    private Set<Serie> series;

    /**
     * amigosOrigen es el usuario donde en el objeto Amigo el usuario es origen lo que significa que ese objeto
     * pertenece al usuario
     */

    @OneToMany (mappedBy = "usuarioOrigen", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Amigo> amigosOrigen;

    /**
     * amigosDestino es el usuario donde el objeto Amigo el usuario es destino lo que significa que ese objeto
     * pertenece al amigo del usuario
     */

    @OneToMany (mappedBy = "usuarioDestino", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Amigo> amigosDestino;

    /**
     * método utilizado por Spring Security para obtener el rol del usuario que hace login
     * @return authorities que tiene el rol del usuario
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority(role.getNombre()));
    return authorities;
    }

    /**
     * método utilizado por Spring Security proveniente de la implementación de UserDetails para obtener la contraseña
     * del usuario y validar si es correcta en el login
     * @return devuelve la contrasena del usuario
     */

    @Override
    public String getPassword() {
        return contrasena;
    }

    /**
     * método utilizado por Spring Security proveniente de la implementación de UserDetails para obtener el nombre del
     * usuario y validar si es correcta en el login
     * @return devuelve el nombre del usuario
     */
    @Override
    public String getUsername() {
        return nombre;
    }

    /**
     * Los métodos isAccountNonExpired, isAccountNonLocked , isCredentialsNonExpired , isEnabled son provenientes de
     * de la implementación de UserDetails y los utiliza el sistema de Spring Security para que el usuario pueda hacer login
     * si todos son true y si el nombre y la contraseña aportados son correctos.
     * @return devuelven todos un boolean true para indicar que la cuenta del usuario es valida
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


