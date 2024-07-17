package com.example.korner.repositorios;


import com.example.korner.modelo.Animes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Animes, Integer> {
}
