package com.example.korner.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Column (name = "nombre" , length = 45)
    private String nombre;

    @Column (name = "edad")
    private Integer edad;

    @Column (name = "pais" , length = 45)
    private String pais;

    @Column (name = "contrasena" , length = 100)
    private String contrasena;

    @Column (name = "correo" , length = 45)
    private String correo;

    @Column (name = "imagen")
    private String imagen;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_roles", foreignKey = @ForeignKey(name = "fk_rol_usuario"))
    private Rol role;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_generos", foreignKey = @ForeignKey(name = "fk_genero_usuario"))
    private Genero generos;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_notificaciones"))
    private Set<Notificaciones> notificacion;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_anime"))
    private Set<Animes> anime;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_libros"))
    private Set<Libro> libros;

    @OneToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_videojuegos"))
    private Set<Videojuego> videojuegos;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_peliculas"))
    private Set<Pelicula> peliculas;

    @OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn (name = "id_usuarios", foreignKey = @ForeignKey(name = "fk_usuarios_series"))
    private Set<Series> series;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//       Set<GrantedAuthority> authorities = new HashSet<>();
//       for (Rol roles : role) {
//           GrantedAuthority authority = new SimpleGrantedAuthority(role.getNombre().toUpperCase());
//           authorities.add(new SimpleGrantedAuthority(role.getNombre()));
//       }
//
//        return authorities;
//    }

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
