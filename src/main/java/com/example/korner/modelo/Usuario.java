package com.example.korner.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.time.Year;
import java.util.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "usuarios",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nombre"}),
        @UniqueConstraint(columnNames = "correo")})
public class Usuario implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Size(max = 20,  message = "Debe tener como máximo 20 caracteres")
    @Size(min = 2,  message = "Debe tener como mínimo 2 caracteres")
    @NotBlank
    @Pattern(regexp = "^\\S+$",message = "No puede haber espacios en el nombre")
    @Column (name = "nombre" , length = 20)
    private String nombre;

    @Column (name = "anio_nacimiento")
    @Min(1900)
    @Max(2200)
    @NotNull
    private Integer anioNacimiento;

    @Column (name = "activa")
    private Boolean activa;

    @Column (name = "contrasena" , length = 100)
    @NotBlank
    private String contrasena;

    @Column (name = "correo" , length = 45)
    @NotBlank
    @Email(message = "Introduzca un Email válido")
    private String correo;

    @Column (name = "imagen")
    private String rutaImagen;

    @Column (name = "ajustes_sesion")
    private String ajustesInicioSesion;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_roles", foreignKey = @ForeignKey(name = "fk_rol_usuario"))
    private Rol role;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_generos", foreignKey = @ForeignKey(name = "fk_genero_usuario"))
    @NotNull(message = "Debe seleccionar una opción")
    private Genero generos;

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_notificaciones"))
    private Set<Notificaciones> notificacion;

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioAnime")
    private Set<Anime> animes;

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioLibro")
    private Set<Libro> libros;

    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioVideojuego")
    private Set<Videojuego> videojuegos;

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "usuarioPelicula")
    private Set<Pelicula> peliculas;

    @OneToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_series"))
    private Set<Serie> series;

    @OneToMany (mappedBy = "usuarioOrigen", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Amigo> amigosOrigen;
    @OneToMany (mappedBy = "usuarioDestino", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Amigo> amigosDestino;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority(role.getNombre()));
    return authorities;
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return nombre;
    }

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


