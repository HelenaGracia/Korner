package com.example.korner.repositorios;


import com.example.korner.modelo.Amigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmigosRepository extends JpaRepository<Amigo, Integer> {
}
