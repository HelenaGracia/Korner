package com.example.korner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import com.example.korner.controladores.NotificacionController;
import com.example.korner.modelo.*;
import com.example.korner.servicio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class NotificacionControllerTest {
    @Mock
    private NotificacionService notificacionService;

    @Mock
    private UsuarioSecurityService usuarioSecurityService;

    @Mock
    private PeliculaServiceImpl peliculaService;

    @Mock
    private SerieServiceImpl serieService;

    @Mock
    private LibroServiceImpl libroService;

    @Mock
    private VideojuegoServiceImpl videojuegoService;

    @Mock
    private AnimeServiceImpl animeService;

    @Mock
    private AmigoServiceImpl amigoService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;
    @Mock
    private RedirectAttributes attributes;

    @InjectMocks
    private NotificacionController notificacionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testContarNotificacionesPendientesPorTipo() {
        String nombreUsuario = "usuario1";
        when(session.getAttribute("userName")).thenReturn(nombreUsuario);

        List<Notificacion> listaNotificaciones = new ArrayList<>();

        // Simular diferentes tipos de notificaciones
        listaNotificaciones.add(crearNotificacion("pelicula", 1, "user2", 2));
        listaNotificaciones.add(crearNotificacion("serie", 2, "user3", 3));
        listaNotificaciones.add(crearNotificacion("libro", 3, "user4", 4));
        listaNotificaciones.add(crearNotificacion("videojuego", 4, "user5", 5));
        listaNotificaciones.add(crearNotificacion("anime", 5, "user6", 6));
        listaNotificaciones.add(crearNotificacion("solicitud Aceptada", 6, "user7", 7));
        listaNotificaciones.add(crearNotificacion("solicitud Enviada", 7, "user8", 8));

        when(notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario, "pendiente"))
                .thenReturn(listaNotificaciones);

        // Simular respuestas de los servicios para cada tipo de notificación
        mockServiciosElemento();

        // Segunda llamada al servicio de notificaciones después de la actualización
        when(notificacionService.getAllNotificacionesByUserAndEstadoListAndEstadoUsuario(nombreUsuario, "pendiente", "activo"))
                .thenReturn(listaNotificaciones);

        // Ejecutar el método a probar
        ResponseEntity<String> response = notificacionController.contarNotificacionesPendientes(session);

        // Verificar que se actualizaron y contaron todas las notificaciones
        verify(notificacionService, times(7)).saveEntity(any(Notificacion.class));
        assertEquals("7", response.getBody());
    }



    private Notificacion crearNotificacion(String tipoElemento, Integer idTipoElemento, String userFrom, Integer userFromId) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTipoElemento(tipoElemento);
        notificacion.setIdTipoElemento(idTipoElemento);
        notificacion.setUserFrom(userFrom);
        notificacion.setUserFromId(userFromId);
        notificacion.setEstadoUsuario("pendiente");
        return notificacion;
    }

    private void mockServiciosElemento() {
        // Película
        when(peliculaService.getById(1)).thenReturn(Optional.of(new Pelicula()));
        mockUsuarioActivo("user2", 2);

        // Serie
        when(serieService.getById(2)).thenReturn(Optional.of(new Serie()));
        mockUsuarioActivo("user3", 3);

        // Libro
        when(libroService.getById(3)).thenReturn(Optional.of(new Libro()));
        mockUsuarioActivo("user4", 4);

        // Videojuego
        when(videojuegoService.getById(4)).thenReturn(Optional.of(new Videojuego()));
        mockUsuarioActivo("user5", 5);

        // Anime
        when(animeService.getById(5)).thenReturn(Optional.of(new Anime()));
        mockUsuarioActivo("user6", 6);

        // Solicitud Aceptada
        when(usuarioSecurityService.getById(7)).thenReturn(Optional.of(new Usuario()));
        mockUsuarioActivo("user7", 7);

        // Solicitud Enviada
        when(usuarioSecurityService.getById(8)).thenReturn(Optional.of(new Usuario()));
        when(amigoService.getById(7)).thenReturn(Optional.of(new Amigo()));
        mockUsuarioActivo("user8", 8);
    }

    private void mockUsuarioActivo(String userFrom, Integer userFromId) {
        Usuario usuario = new Usuario();
        usuario.setActiva(true);
        usuario.setRutaImagen("rutaImagen" + userFromId);
        when(usuarioSecurityService.getByName(userFrom)).thenReturn(Optional.of(usuario));
        when(usuarioSecurityService.getById(userFromId)).thenReturn(Optional.of(usuario));
    }




    @Test
    public void testEliminarNotificaciones() {
        // Datos de prueba
        String nombreUsuario = "testUser";
        Notificacion notificacion1 = new Notificacion();
        notificacion1.setEstado("leido");
        Notificacion notificacion2 = new Notificacion();
        notificacion2.setEstado("leido");

        List<Notificacion> listaNotificaciones = List.of(notificacion1, notificacion2);

        // Configurar mocks
        when(session.getAttribute("userName")).thenReturn(nombreUsuario);
        when(notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario, "leido")).thenReturn(listaNotificaciones);

        // Ejecutar el método
        String result = notificacionController.eliminarNotificaciones(session, attributes);

        // Verificar que las notificaciones leídas se eliminaron
        verify(notificacionService, times(1)).getAllNotificacionesByUserAndEstadoList(nombreUsuario, "leido");
        verify(notificacionService, times(2)).deleteEntity(any(Notificacion.class));

        // Verificar que el mensaje de éxito fue añadido
        verify(attributes, times(1)).addFlashAttribute("success", "Se han eliminado todas las notificaciones almacenadas");

        // Verificar que el nombre de la vista es el esperado
        assertEquals("redirect:/leerNotificaciones", result);
    }
}
