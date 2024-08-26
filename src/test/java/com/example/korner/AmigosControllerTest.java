package com.example.korner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.example.korner.controladores.AmigosController;
import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Notificacion;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.AmigoServiceImpl;
import com.example.korner.servicio.NotificacionService;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class AmigosControllerTest {
    @Mock
    private UsuarioSecurityService usuarioService;

    @Mock
    private AmigoServiceImpl amigoService;

    @Mock
    private NotificacionService notificacionService;

    @Mock
    private HttpSession session;
    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AmigosController amigosController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAceptarSolicitud() {
        // Simulación de datos
        Usuario usuarioDestino = new Usuario();
        usuarioDestino.setId(1);
        usuarioDestino.setNombre("Usuario Destino");
        usuarioDestino.setRutaImagen("rutaImagenDestino");

        Usuario usuarioOrigen = new Usuario();
        usuarioOrigen.setId(2);
        usuarioOrigen.setNombre("Usuario Origen");

        Amigo amigo = new Amigo();
        amigo.setUsuarioOrigen(usuarioOrigen);
        amigo.setUsuarioDestino(usuarioDestino);
        amigo.setPendiente(true);

        // Configurar mocks
        when(session.getAttribute("idusuario")).thenReturn("1");
        when(usuarioService.getById(1)).thenReturn(Optional.of(usuarioDestino));
        when(usuarioService.getById(2)).thenReturn(Optional.of(usuarioOrigen));
        when(amigoService.getAmigo(usuarioDestino, usuarioOrigen)).thenReturn(amigo);

        // Ejecutar el método
        String result = amigosController.aceptarSolicitud(2, session, redirectAttributes);

        // Verificaciones
        verify(amigoService, times(2)).saveEntity(any(Amigo.class));
        verify(notificacionService).saveEntity(any(Notificacion.class));
        verify(redirectAttributes).addFlashAttribute(eq("success"), anyString());
        assertEquals("redirect:/amigos/solicitudesPendientes", result);
    }

    @Test
    public void testShowAmigos() {
        // Simulación de datos
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Usuario");
        usuario.setRutaImagen("rutaImagen");

        Amigo amigo1 = new Amigo();
        Amigo amigo2 = new Amigo();
        List<Amigo> amigos = Arrays.asList(amigo1, amigo2);

        Page<Amigo> pagina = new PageImpl<>(amigos, PageRequest.of(0, 6), amigos.size());
        Page<Amigo> paginaBloq = new PageImpl<>(amigos, PageRequest.of(0, 6), amigos.size());

        // Configurar mocks
        when(session.getAttribute("idusuario")).thenReturn("1");
        when(session.getAttribute("rutaImagen")).thenReturn("rutaImagen");
        when(session.getAttribute("userName")).thenReturn("Usuario");
        when(usuarioService.getById(anyInt())).thenReturn(Optional.of(usuario));
        when(amigoService.getAllAmigos(any(Usuario.class), any(Pageable.class))).thenReturn(pagina);
        when(amigoService.getAllAmigosBloqueados(any(Usuario.class), any(Pageable.class))).thenReturn(pagina);

        // Ejecutar el método
        String viewName = amigosController.showAmigos(Optional.of(1), Optional.of(1), session, model);

        // Verificaciones
        verify(model).addAttribute("imagenUsuario", "rutaImagen");
        verify(model).addAttribute("nameUsuario", "Usuario");
        verify(model).addAttribute("pagina", pagina);
        verify(model).addAttribute("pageNumbers", IntStream.rangeClosed(1, pagina.getTotalPages()).boxed().collect(Collectors.toList()));
        verify(model).addAttribute("currentPage", 1);
        verify(model).addAttribute("size", amigos.size());
        verify(model).addAttribute("amigo", amigos);
        verify(model).addAttribute("paginaBloq", paginaBloq);
        verify(model).addAttribute("pageNumbersBloq", IntStream.rangeClosed(1, paginaBloq.getTotalPages()).boxed().collect(Collectors.toList()));
        verify(model).addAttribute("currentPageBloq", 1);
        verify(model).addAttribute("sizeBloq", amigos.size());
        verify(model).addAttribute("bloqueados", amigos);

        // Verificar que se devuelve el nombre de la vista correcta
        assertEquals("amigos", viewName);
    }
    @Test
    public void testSolicitudesPendientes() {
        // Simulación de datos
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Usuario");
        usuario.setRutaImagen("rutaImagen");

        Amigo amigo1 = new Amigo();
        Amigo amigo2 = new Amigo();
        List<Amigo> amigos = Arrays.asList(amigo1, amigo2);

        Page<Amigo> pagina = new PageImpl<>(amigos, PageRequest.of(0, 10), amigos.size());

        // Configurar mocks
        when(session.getAttribute("idusuario")).thenReturn("1");
        when(session.getAttribute("rutaImagen")).thenReturn("rutaImagen");
        when(session.getAttribute("userName")).thenReturn("Usuario");
        when(usuarioService.getById(anyInt())).thenReturn(Optional.of(usuario));
        when(amigoService.getAllSolicitudesPendientes(any(Usuario.class), any(Pageable.class))).thenReturn(pagina);

        // Ejecutar el método
        String viewName = amigosController.solicitudesPendientes(Optional.of(1), model, session);

        // Verificaciones
        verify(model).addAttribute("imagenUsuario", "rutaImagen");
        verify(model).addAttribute("nameUsuario", "Usuario");
        verify(model).addAttribute("pagina", pagina);
        verify(model).addAttribute("pageNumbers", IntStream.rangeClosed(1, pagina.getTotalPages()).boxed().collect(Collectors.toList()));
        verify(model).addAttribute("currentPage", 1);
        verify(model).addAttribute("size", amigos.size());
        verify(model).addAttribute("amigos", amigos);

        // Verificar que se devuelve el nombre de la vista correcta
        assertEquals("amigosPendientes", viewName);
    }
    @Test
    public void testRechazarSolicitud() {
        // Crear instancias de prueba
        Usuario usuarioActual = new Usuario();
        usuarioActual.setId(1);
        usuarioActual.setNombre("Usuario Actual");

        Usuario usuarioDestino = new Usuario();
        usuarioDestino.setId(2);
        usuarioDestino.setNombre("Usuario Destino");

        Amigo amigo = new Amigo();
        amigo.setUsuarioOrigen(usuarioActual);
        amigo.setUsuarioDestino(usuarioDestino);

        // Configurar mocks
        when(session.getAttribute("idusuario")).thenReturn("1");
        when(usuarioService.getById(1)).thenReturn(Optional.of(usuarioActual));
        when(usuarioService.getById(2)).thenReturn(Optional.of(usuarioDestino));
        when(amigoService.getAmigo(usuarioActual, usuarioDestino)).thenReturn(amigo);

        // Ejecutar el método
        String result = amigosController.rechazarSolicitud(2, session, redirectAttributes);

        // Verificaciones
        verify(amigoService).deleteEntity(amigo);
        verify(redirectAttributes).addFlashAttribute(eq("success"), eq("La solicitud de amistad del usuario: Usuario Destino ha sido rechazada"));
        assertEquals("redirect:/amigos/solicitudesPendientes", result);
    }

    @Test
    public void testVerSolicitudesEnviadas() {
        // Crear instancias de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Usuario");

        Amigo amigo1 = new Amigo();
        Amigo amigo2 = new Amigo();

        // Configurar la paginación
        Page<Amigo> pagina = new PageImpl<>(List.of(amigo1, amigo2), PageRequest.of(0, 10), 2);

        // Configurar mocks
        when(session.getAttribute("idusuario")).thenReturn("1");
        when(session.getAttribute("rutaImagen")).thenReturn("rutaImagen");
        when(session.getAttribute("userName")).thenReturn("Usuario");
        when(usuarioService.getById(1)).thenReturn(Optional.of(usuario));
        when(amigoService.getAllSolicitudesEnviadas(eq(usuario), any(Pageable.class))).thenReturn(pagina);

        // Ejecutar el método
        String result = amigosController.verSolicitudesEnviadas(Optional.of(1), model, session);

        // Verificaciones
        verify(model).addAttribute(eq("imagenUsuario"), eq("rutaImagen"));
        verify(model).addAttribute(eq("nameUsuario"), eq("Usuario"));
        verify(model).addAttribute(eq("pagina"), eq(pagina));
        verify(model).addAttribute(eq("currentPage"), eq(1));
        verify(model).addAttribute(eq("size"), eq(2));
        verify(model).addAttribute(eq("amigos"), anyList());

        // Verifica que la lista de números de página ha sido creada correctamente
        List<Integer> expectedPageNumbers = IntStream.rangeClosed(1, pagina.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        verify(model).addAttribute(eq("pageNumbers"), eq(expectedPageNumbers));

        assertEquals("amigosSolicitudesEnviadas", result);
    }

    @Test
    public void testEliminarSolicitudEnviada() {
        // Simulación de datos
        Usuario usuarioActual = new Usuario();
        usuarioActual.setId(1);
        usuarioActual.setNombre("Usuario Actual");

        Usuario usuarioDestino = new Usuario();
        usuarioDestino.setId(2);
        usuarioDestino.setNombre("Usuario Destino");

        Amigo amigo = new Amigo();
        amigo.setUsuarioOrigen(usuarioActual);
        amigo.setUsuarioDestino(usuarioDestino);

        // Configurar mocks
        when(session.getAttribute("idusuario")).thenReturn("1");
        when(usuarioService.getById(1)).thenReturn(Optional.of(usuarioActual));
        when(usuarioService.getById(2)).thenReturn(Optional.of(usuarioDestino));
        when(amigoService.getAmigo(usuarioDestino, usuarioActual)).thenReturn(amigo);

        // Ejecutar el método
        String result = amigosController.eliminarSolicitudEnviada(2, session, redirectAttributes);

        // Verificaciones
        verify(amigoService).deleteEntity(amigo);
        verify(redirectAttributes).addFlashAttribute("success", "La solicitud de amistad al usuario: " + usuarioDestino.getNombre() + " ha sido eliminada");
        assertEquals("redirect:/amigos/solicitudesEnviadas", result);
    }


    @Test
    public void testDeleteAmigo() {
        // Simulación de datos
        Integer amigoId = 1;

        Usuario usuarioOrigen = new Usuario();
        usuarioOrigen.setId(1);
        usuarioOrigen.setNombre("Usuario Origen");

        Usuario usuarioDestino = new Usuario();
        usuarioDestino.setId(2);
        usuarioDestino.setNombre("Usuario Destino");

        Amigo amigoOrigen = new Amigo();
        amigoOrigen.setId(amigoId);
        amigoOrigen.setUsuarioOrigen(usuarioOrigen);
        amigoOrigen.setUsuarioDestino(usuarioDestino);

        Amigo amigoDestino = new Amigo();
        amigoDestino.setId(2);
        amigoDestino.setUsuarioOrigen(usuarioDestino);
        amigoDestino.setUsuarioDestino(usuarioOrigen);

        // Configurar mocks
        when(amigoService.getById(amigoId)).thenReturn(Optional.of(amigoOrigen));
        when(amigoService.getAmigo(usuarioOrigen, usuarioDestino)).thenReturn(amigoDestino);

        // Ejecutar el método
        String result = amigosController.deleteAmigo(amigoId);

        // Verificaciones
        verify(amigoService).deleteEntity(amigoOrigen); // Verifica que se eliminó la relación original
        verify(amigoService).deleteEntity(amigoDestino); // Verifica que se eliminó la relación inversa
        assertEquals("redirect:/amigos", result); // Verifica que la redirección es correcta
    }

    @Test
    public void testBloquearAmigo() {
        // Datos simulados
        Integer id = 1;
        Amigo amigo = new Amigo();
        amigo.setId(id);
        amigo.setBloqueado(false);

        // Configurar mocks
        when(amigoService.getById(id)).thenReturn(Optional.of(amigo));

        // Ejecutar el método
        String result = amigosController.bloquearAmigo(id);

        // Verificaciones
        assertEquals(true, amigo.getBloqueado()); // Verificar que el amigo está bloqueado
        verify(amigoService).saveEntity(amigo);  // Verificar que se guardó el cambio
        assertEquals("redirect:/amigos", result); // Verificar la redirección
    }

    @Test
    public void testDesbloquearAmigo() {
        // Datos simulados
        Integer id = 1;
        Amigo amigo = new Amigo();
        amigo.setId(id);
        amigo.setBloqueado(true); // Inicialmente bloqueado

        // Configurar mocks
        when(amigoService.getById(id)).thenReturn(Optional.of(amigo));

        // Ejecutar el método
        String result = amigosController.desbloquearAmigo(id);

        // Verificaciones
        assertEquals(false, amigo.getBloqueado()); // Verificar que el amigo ya no está bloqueado
        verify(amigoService).saveEntity(amigo);   // Verificar que se guardó el cambio
        assertEquals("redirect:/amigos", result); // Verificar la redirección
    }
}
