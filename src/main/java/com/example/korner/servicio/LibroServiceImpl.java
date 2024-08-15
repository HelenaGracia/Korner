package com.example.korner.servicio;

import com.example.korner.modelo.*;
import com.example.korner.repositorios.LibroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class LibroServiceImpl extends AbstractService<Libro,Integer, LibroRepository>{
        private  final LibroRepository libroRepository;

        public LibroServiceImpl(LibroRepository libroRepository, LibroRepository libroRepository1) {
            super(libroRepository);

            this.libroRepository = libroRepository1;
        }

        public Page<Libro> getAllLibros(Usuario usuario, Pageable pageable){
            return libroRepository.findAllByUsuarioLibro(usuario,pageable);
        }

        public Page<Libro> getAllLibrosByTitulo(String titulo, Usuario usuario, Pageable pageable){
            return libroRepository.findAllByTituloContainingIgnoreCaseAndUsuarioLibro(titulo, usuario, pageable);
        }

        public Page<Libro> getAllLibrosByPuntuacion(Integer puntuacion, Usuario usuario, Pageable pageable){
            return libroRepository.findAllByPuntuacionAndUsuarioLibro(puntuacion, usuario, pageable);
        }

        public  Page<Libro> getAllLibrosByGenero(GeneroElementoCompartido genero, Usuario usuario, Pageable pageable){
            return libroRepository.findAllByGenerosLibroAndUsuarioLibro(genero,usuario,pageable);
        }

        public  Page<Libro> getAllLibrosByYear(Integer year, Usuario usuario, Pageable pageable){
            return  libroRepository.findAllByYearAndUsuarioLibro(year, usuario, pageable);
        }

        public  Page<Libro> getAllLibrosByPlataforma(Plataforma plataforma, Usuario usuario, Pageable pageable){
            return libroRepository.findAllByPlataformasLibroAndUsuarioLibro(plataforma, usuario, pageable);
        }

        public Page<Libro>getAllLibrosByAllFiltros(
                Integer puntuacion, GeneroElementoCompartido genero, Integer year,
                Plataforma plataforma, Usuario usuario, Pageable pageable){
            return libroRepository.findAllByPuntuacionAndGenerosLibroAndYearAndPlataformasLibroAndUsuarioLibro(
                    puntuacion,genero,year,plataforma, usuario, pageable);
        }

        public Page<Libro> getAllLibroCompartidosByListId (List<Integer> listId, Pageable page){
            return libroRepository.findAllByIdIn(listId, page);
        }

        public Page<Libro> getAllLibroCompartidosByListIdAndUsuario (List<Integer> listId,Usuario usuario, Pageable page){
            return libroRepository.findAllByIdInAndUsuarioLibro(listId,usuario, page);
        }
    }
