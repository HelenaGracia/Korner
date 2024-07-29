package com.example.korner.security;


import com.example.korner.config.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bcryptPasswordEncoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
        );

        // Autorización de Solicitudes
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/img/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/fonts/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/creacion").permitAll()
                .requestMatchers(HttpMethod.POST, "/creacion/**").permitAll()
                .requestMatchers("/generos/**").hasAuthority("ADMIN")
                .anyRequest().authenticated());

        http.exceptionHandling((exceptions)->exceptions.accessDeniedHandler(accessDeniedHandler()));
        // con el de abajo se hace lo mismo, pero sin el log de error editable con el de arriba
        //http.exceptionHandling().accessDeniedPage("/accessDenied")

        return http.build();
    }


    //  para autenticar a los usuarios
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean

    static GrantedAuthorityDefaults grantedAuthorityDefaults() {

        return new GrantedAuthorityDefaults("ROLE_");

    }

}
