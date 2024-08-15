package com.example.korner.repositorios;

import com.example.korner.modelo.FormatoLibro;
import com.example.korner.modelo.PlataformaVideojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormatoLibroRepository extends JpaRepository<FormatoLibro, Integer> {
}
