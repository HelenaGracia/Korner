package com.example.korner.controladores;


import com.example.korner.modelo.Amigo;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.AmigoServiceImpl;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/amigos")
public class BusquedaAmigosController {


    private final AmigoServiceImpl amigoService;
    private final UsuarioSecurityService usuarioService;

    public BusquedaAmigosController(AmigoServiceImpl amigoService, UsuarioSecurityService usuarioService) {
        this.amigoService = amigoService;
        this.usuarioService = usuarioService;
    }
    private final Logger logger = LoggerFactory.getLogger(BusquedaAmigosController.class);


    @GetMapping("/search")
    public String search(@RequestParam(value = "amigosBusqueda",required = false) String nombreUser,
                         @RequestParam(value = "page") Optional<Integer> page,HttpSession session, Model model) {
        try{
            Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
            //lista Amigos del usuario sin distincion de bloqueados o pendientes
            List<Amigo> listAmigos = amigoService.getAllAmigosList(user.get());
            //lista de Id de esos amigos
            List<Integer> listIdAmigos= new java.util.ArrayList<>(listAmigos.stream().map(Amigo::getUsuarioDestino).map(Usuario::getId).toList());
            //lista de id con amigos y el usuario actual
            listIdAmigos.add(user.get().getId());

            int currentPage = page.orElse(1);
            Pageable pageRequest = PageRequest.of(currentPage - 1, 10);
            Page<Usuario> pagina = null;
            if(nombreUser == null || nombreUser.isEmpty()){
                model.addAttribute("busquedaFallida","Debe introducir un nombre de usuario");
                return "busquedaAmigos";
            }else {
                pagina = usuarioService.getAllUsuariosSinListId(nombreUser,listIdAmigos,pageRequest);
                model.addAttribute("busquedaUsuarios",nombreUser);
                if(pagina.getContent().isEmpty() ){
                    model.addAttribute("busquedaFallida","No se ha encontrado ningun usuario con ese nombre");

                }

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
                model.addAttribute("usuarios", pagina.getContent());

            }

        } catch (Exception e){
            logger.error("Error en la busqueda",e);
            model.addAttribute("busquedaFallida", "Error al realizar la búsqueda");

        }

        return "busquedaAmigos";
    }

    @GetMapping("/enviarSolicitud/{id}/{nombreUser}")
    public String enviarSolicitud(@PathVariable Integer id ,
                                  @PathVariable String nombreUser,
                                  HttpSession session){
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString() )));
        Optional<Usuario> userDestino= usuarioService.getById(id);
        Amigo amigoOrigen = new Amigo();
        amigoOrigen.setPendiente(true);
        amigoOrigen.setUsuarioOrigen(user.get());
        amigoOrigen.setUsuarioDestino(userDestino.get());
        amigoOrigen.setBloqueado(false);
        amigoService.saveEntity(amigoOrigen);

        return "redirect:/amigos/search?amigosBusqueda="+nombreUser;

    }




}
