package com.example.korner.repositorios;


import com.example.korner.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findBynombre(String username);
    Optional<Usuario> findByCorreo(String username);
    Page<Usuario> findAllByNombreContainingIgnoreCase(String username, Pageable pageble);
    Page<Usuario> findAllByNombreContainingIgnoreCaseAndIdNotIn(String username, List<Integer> excludedId, Pageable pageble);
    List<Usuario> findAllByNombreContainingIgnoreCaseAndIdIn(String username, List<Integer> includeId);
    List<Usuario>findAllByIdNot(Integer id);

}
