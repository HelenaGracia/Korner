package com.example.korner.repositorios;

import com.example.korner.modelo.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroUsuarioRepository extends JpaRepository<Genero, Integer> {
}
