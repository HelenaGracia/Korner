package com.example.korner.servicio;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.parser.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AbstractService <E,ID, REPO extends JpaRepository<E, ID>>{
    private final REPO repo;

    public AbstractService(REPO repo) {
        this.repo = repo;
    }

    //Metodo para listar todas las entidades
    public List<E> getAll(){
        return repo.findAll();
    }
    //Metodo para buscar una entidad por su ID
    public Optional<E> getById(ID id){
        return repo.findById(id);
    }

    //Metodo para guardar una entidad
    public Optional<E> saveEntity(E entity){
        return Optional.of(repo.save(entity));
    }

    //Metodo para guardar una entidad por su ID
    public Optional<E> saveEntityById(ID id){
        Optional<E> entity = getById(id);
        return Optional.of(repo.save(entity.get()));
    }

    //Metodo para borrar una entidad por su ID
   public void deleteEntity(E entity){
        repo.delete(entity);
   }

    //Metodo para borrar una entidad por su ID
   public void deleteEntityById(ID id){
        repo.deleteById(id);
   }

}
