package com.example.korner.controladores;


import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Notificacion;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.AmigoServiceImpl;
import com.example.korner.servicio.NotificacionService;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/amigos")
public class AmigosController {


    private final AmigoServiceImpl amigoService;
    private final UsuarioSecurityService usuarioService;
    private final NotificacionService notificacionService;

    public AmigosController(AmigoServiceImpl amigoService, UsuarioSecurityService usuarioService, NotificacionService notificacionService) {
        this.amigoService = amigoService;
        this.usuarioService = usuarioService;
        this.notificacionService = notificacionService;
    }


    @GetMapping
    public String showAmigos(@RequestParam(value = "page",required = false) Optional<Integer> page,
                             @RequestParam(value = "pagebloq" , required = false) Optional<Integer> pagebloq,
                             HttpSession session,Model model) {

        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

        //Paginación
        int currentPage = page.orElse(1);
        Pageable pageRequest = PageRequest.of(currentPage - 1, 6);
        Page<Amigo> pagina = amigoService.getAllAmigos(user.get(), pageRequest);
        //Envio la pagina creada a la vista para poder verla
        model.addAttribute("pagina", pagina);
        //Obtengo la cantidad de paginas creadas, por ejemplo: 8
        int totalPages = pagina.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        //Envio a la vista la pagina en la que estoy
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", pagina.getContent().size());
        model.addAttribute("amigo", pagina.getContent());

        //Paginación Bloqueados
        int currentPageBloq = pagebloq.orElse(1);
        Pageable pageRequestBloq = PageRequest.of(currentPageBloq - 1, 6);
        Page<Amigo> paginaBloq = amigoService.getAllAmigosBloqueados(user.get(), pageRequestBloq);
        model.addAttribute("paginaBloq", paginaBloq);
        int totalPagesBloq = paginaBloq.getTotalPages();
        if (totalPagesBloq > 0) {
            List<Integer> pageNumbersBloq = IntStream.rangeClosed(1, totalPagesBloq)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbersBloq", pageNumbersBloq);
        }
        //Envio a la vista la pagina en la que estoy
        model.addAttribute("currentPageBloq", currentPageBloq);
        model.addAttribute("sizeBloq", paginaBloq.getContent().size());
        model.addAttribute("bloqueados", paginaBloq.getContent());


        return "amigos";
    }

    @GetMapping("/solicitudesPendientes")
    public String solicitudesPendientes(@RequestParam("page") Optional<Integer> page,
                                        Model model, HttpSession session) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        //Paginación
        int currentPage = page.orElse(1);
        Pageable pageRequest = PageRequest.of(currentPage - 1, 10);
        Page<Amigo> pagina = amigoService.getAllSolicitudesPendientes(user.get(),pageRequest);
        model.addAttribute("pagina", pagina);
        int totalPages = pagina.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        //Envio a la vista la pagina en la que estoy
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", pagina.getContent().size());
        model.addAttribute("amigos", pagina.getContent());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        return "amigosPendientes";
    }

    @GetMapping("/aceptarSolicitud/{id}")
    public String aceptarSolicitud(@PathVariable Integer id ,HttpSession session, RedirectAttributes attributes){
        Optional<Usuario> userDestino = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userOrigen= usuarioService.getById(id);
        Amigo amigoAceptado= amigoService.getAmigo(userDestino.get(),userOrigen.get());
        amigoAceptado.setPendiente(false);
        Amigo amigoNuevo = new Amigo();
        amigoNuevo.setBloqueado(false);
        amigoNuevo.setPendiente(false);
        amigoNuevo.setUsuarioOrigen(userDestino.get());
        amigoNuevo.setUsuarioDestino(userOrigen.get());
        amigoService.saveEntity(amigoAceptado);
        amigoService.saveEntity(amigoNuevo);
        Notificacion notificacion = new Notificacion();
        notificacion.setUserFrom(userDestino.get().getNombre());
        notificacion.setUserTo(userOrigen.get().getNombre());
        notificacion.setEstado("pendiente");
        notificacion.setMensaje("El usuario " + userDestino.get().getNombre() + " ha aceptado tu solicitud de amistad");
        notificacion.setTipoElemento("solicitud");
        notificacion.setUserFromId(userDestino.get().getId());
        notificacion.setRutaImagenUserFrom(userDestino.get().getRutaImagen());
        notificacionService.saveEntity(notificacion);
        attributes.addFlashAttribute("success","La solicitud de amistad del usuario: " + userOrigen.get().getNombre() + " ha sido aceptada");
        return "redirect:/amigos/solicitudesPendientes";

    }

    @GetMapping("/rechazarSolicitud/{id}")
    public String rechazarSolicitud(@PathVariable Integer id , HttpSession session, RedirectAttributes attributes){

        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userDestino= usuarioService.getById(id);
        Amigo amigoEliminar = amigoService.getAmigo(user.get(),userDestino.get());
        amigoService.deleteEntity(amigoEliminar);
        attributes.addFlashAttribute("success","La solicitud de amistad del usuario: " + userDestino.get().getNombre() + " ha sido rechazada");

        return "redirect:/amigos/solicitudesPendientes";

    }

    @GetMapping("/solicitudesEnviadas")
    public String verSolicitudesEnviadas(@RequestParam("page") Optional<Integer> page,
                                         Model model, HttpSession session){
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        //Paginación
        int currentPage = page.orElse(1);
        Pageable pageRequest = PageRequest.of(currentPage - 1, 10);
        Page<Amigo> pagina = amigoService.getAllSolicitudesEnviadas(user.get(),pageRequest);
        model.addAttribute("pagina", pagina);
        int totalPages = pagina.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        //Envio a la vista la pagina en la que estoy
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", pagina.getContent().size());
        model.addAttribute("amigos", pagina.getContent());

        return "amigosSolicitudesEnviadas";
    }

    @GetMapping("/eliminarSolicitud/{id}")
    public String eliminarSolicitudEnviada(@PathVariable Integer id , HttpSession session, RedirectAttributes attributes){

        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userDestino= usuarioService.getById(id);
        Amigo amigoEliminar = amigoService.getAmigo(userDestino.get(),user.get());
        amigoService.deleteEntity(amigoEliminar);
        attributes.addFlashAttribute("success","La solicitud de amistad al usuario: " + userDestino.get().getNombre() + " ha sido eliminada");

        return "redirect:/amigos/solicitudesEnviadas";

    }


    @PostMapping("/delete")
    public String deleteAmigo(Amigo amigos){
        Optional<Amigo> amigoOrigen = amigoService.getById(amigos.getId());
        Amigo amigoDestino = amigoService.getAmigo(amigoOrigen.get().getUsuarioOrigen(),amigoOrigen.get().getUsuarioDestino());
        amigoService.deleteEntity(amigoOrigen.get());
        amigoService.deleteEntity(amigoDestino);
        return "redirect:/amigos";
    }


    @PostMapping("/bloquear")
    public String bloquearAmigo(Amigo amigos){
        Optional<Amigo> amigoBloqueado = amigoService.getById(amigos.getId());
        amigoBloqueado.get().setBloqueado(true);
        amigoService.saveEntity(amigoBloqueado.get());
        return "redirect:/amigos";
    }

    @PostMapping("/desbloquear")
    public String desbloquearAmigo(Amigo amigos){
        Optional<Amigo> amigoDesbloqueado = amigoService.getById(amigos.getId());
        amigoDesbloqueado.get().setBloqueado(false);
        amigoService.saveEntity(amigoDesbloqueado.get());
        return "redirect:/amigos";
    }

}
