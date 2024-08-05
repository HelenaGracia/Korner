package com.example.korner.repositorios;

import com.example.korner.modelo.Plataforma;
import com.example.korner.modelo.PlataformaVideojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlataformaVideojuegoRepository extends JpaRepository<PlataformaVideojuego, Integer> {
}
