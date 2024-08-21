package com.example.korner.controladores;

import com.example.korner.modelo.Notificacion;
import com.example.korner.servicio.NotificacionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class NotificacionController {
    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/numeroNotificaciones")
    public ResponseEntity<String> contarNotificacionesPendientes(HttpSession session){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario,"pendiente");
        return ResponseEntity.ok(String.valueOf(listaNotificaciones.size())) ;
    }


    @GetMapping("/leerNotificaciones")
    public String leerNotificaciones(Model model, @RequestParam("page") Optional<Integer> page, HttpSession session){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario,"pendiente");
        listaNotificaciones.forEach(notificacion -> {
            notificacion.setEstado("leido");
            notificacionService.saveEntity(notificacion);
        });

        int currentPage = page.orElse(1);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, 4);;
        Page<Notificacion> pagina = notificacionService.getAllNotificacionesByUserAndEstado(nombreUsuario, "leido", pageRequest);
        model.addAttribute("pagina", pagina);
        int totalPages = pagina.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", pagina.getContent().size());
        model.addAttribute("notificaciones", pagina.getContent());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",nombreUsuario);

        return "notificaciones";
    }

    @GetMapping("/eliminarNotificaciones")
    public String eliminarNotificaciones(HttpSession session, RedirectAttributes attributes){
        String nombreUsuario = session.getAttribute("userName").toString();
        List<Notificacion> listaNotificaciones = notificacionService.getAllNotificacionesByUserAndEstadoList(nombreUsuario,"leido");
        listaNotificaciones.forEach(notificacion -> {
            notificacion.setEstado("leido");
            notificacionService.deleteEntity(notificacion);
        });
        attributes.addFlashAttribute("success", "Se han eliminado todas las notificaciones almacenadas");
        return "redirect:/leerNotificaciones";
    }
}
