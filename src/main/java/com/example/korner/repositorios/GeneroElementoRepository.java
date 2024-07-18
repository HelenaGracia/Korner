package com.example.korner.repositorios;

import com.example.korner.modelo.GeneroElementoCompartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroElementoRepository extends JpaRepository<GeneroElementoCompartido, Integer> {
}
