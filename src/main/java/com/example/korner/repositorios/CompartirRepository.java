package com.example.korner.repositorios;


import com.example.korner.modelo.ElementoCompartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompartirRepository extends JpaRepository<ElementoCompartido, Integer> {
}
