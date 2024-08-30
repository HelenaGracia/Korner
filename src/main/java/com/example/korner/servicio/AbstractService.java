package com.example.korner.servicio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Esta clase proporciona un conjunto genérico de métodos para realizar operaciones CRUD y de paginación sobre cualquier tipo de entidad
 * @param <E> tipo de la entidad
 * @param <ID> es el tipo del identificador (clave primaria) de la entidad
 * @param <REPO> es el tipo del repositorio, que debe extender JpaRepository
 */
public class AbstractService <E,ID, REPO extends JpaRepository<E, ID>> {
    private final REPO repo;

    public AbstractService(REPO repo) {
        this.repo = repo;
    }

    /**
     * Encuentra todas las entidades del tipo E en la base de datos.
     */
    public List<E> getAll(){
        return repo.findAll();
    }

    /**
     * Encuentra una entidad por su ID en la base de datos.
     * @param id identificador de la entidad
     */
    public Optional<E> getById(ID id){
        return repo.findById(id);
    }

    /**
     * Guarda una nueva entidad o actualiza una existente en la base de datos.
     * @param entity La entidad que se quiere guardar o actualizar
     */

    public Optional<E> saveEntity(E entity){
        return Optional.of(repo.save(entity));
    }

    /**
     * Guarda una entidad en la base de datos que debe ser encontrada por su ID.
     * @param id identificador de la entidad
     */

    public Optional<E> saveEntityById(ID id){
        Optional<E> entity = getById(id);
        return Optional.of(repo.save(entity.get()));
    }

    /**
     * Elimina una entidad de la base de datos
     * @param entity La entidaa que debe ser eliminada
     */

   public void deleteEntity(E entity){
        repo.delete(entity);
   }

    /**
     * Elimina una entidad de la base de datos que debe ser encontrada por su ID.
     * @param id identificador de la entidad
     */

   public void deleteEntityById(ID id){
        repo.deleteById(id);
   }

    /**
     * Devuelve todas las entidades de la base de datos en paginas
     * @param pageable Información sobre la paginación y el orden de los resultados.
     *
     */

    public Page<E> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

}
