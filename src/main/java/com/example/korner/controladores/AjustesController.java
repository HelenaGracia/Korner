package com.example.korner.controladores;

import com.example.korner.modeloValidaciones.CorreoNuevo;
import com.example.korner.modeloValidaciones.NombreNuevo;
import com.example.korner.modeloValidaciones.PasswordNueva;
import com.example.korner.modelo.Usuario;
import com.example.korner.servicio.FileSystemStorageService;
import com.example.korner.servicio.UsuarioSecurityService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/ajustes")
public class AjustesController {

    private final UsuarioSecurityService usuarioService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileSystemStorageService fileSystemStorageService;

    public AjustesController(UsuarioSecurityService usuarioService, BCryptPasswordEncoder bCryptPasswordEncoder, FileSystemStorageService fileSystemStorageService) {
        this.usuarioService = usuarioService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    @GetMapping
    public String ajustes() {
        return "ajustes";
    }

    @GetMapping("/nombre")
    public String verAjustesCambioNombre(Model model){
        model.addAttribute("nombre",new NombreNuevo());
        return "ajustesNombre";
    }

    @PostMapping("/nombre")
    public String modificarCambioNombre(@Validated @ModelAttribute(value = "nombre") NombreNuevo nombre, BindingResult bindingResult,
                                        HttpSession session, RedirectAttributes attributes, Model model){
        if (bindingResult.hasErrors()) {
            return "ajustesNombre";
        }
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
        if (Objects.equals(user.get().getNombre(), nombre.getNombre())) {
            model.addAttribute("failed", "El nuevo nombre no puede ser el mismo que el actual");
            return "ajustesNombre";
        }else if(usuarioService.getByName(nombre.getNombre()).isPresent()){
            model.addAttribute("failed","El nombre introducido ya existe");
            return "ajustesNombre";
        }if (bCryptPasswordEncoder.matches(nombre.getPasswordActual(), user.get().getPassword())){
            user.get().setNombre(nombre.getNombre());
            usuarioService.saveEntity(user.get());
            attributes.addFlashAttribute("success", "Se ha cambiado el nombre correctamente");
            return "redirect:/ajustes/nombre";
        }
        model.addAttribute("failedPassword","La contraseña no es correcta");
        return "ajustesNombre";
    }

    @GetMapping("/inicioSesion")
    public String verAjustesInicioSesion() {
        return "ajustesInicioSesion";
    }

    @PostMapping("/inicioSesion")
    public String modificarInicoSesion(@RequestParam(value = "ajustes") String ajustes,
                                     HttpSession session, RedirectAttributes attributes,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
        if(Objects.equals(user.get().getAjustesInicioSesion(), ajustes)){
            model.addAttribute("failed","Ya tiene la configuracion en: "+ajustes);
            return "ajustesInicioSesion";
        }
        user.get().setAjustesInicioSesion(ajustes);
        usuarioService.saveEntity(user.get());
        attributes.addFlashAttribute("success","Se ha cambiado correctamente a " + ajustes);

        return "redirect:/ajustes/inicioSesion";
    }

    @GetMapping("/password")
    public String verAjustesPassword(Model model) {

        model.addAttribute("password", new PasswordNueva());
        return "ajustesContrasena";
    }

    @PostMapping("/password")
    public String modificarPassword(@Validated @ModelAttribute(value = "password") PasswordNueva password, BindingResult bindingResult,
                                    HttpSession session, RedirectAttributes attributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "ajustesContrasena";
        }
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
        if (Objects.equals(password.getPasswordNueva(), password.getPasswordNueva2())) {
            if (bCryptPasswordEncoder.matches(password.getPasswordActual(), user.get().getPassword()) && !Objects.equals(password.getPasswordActual(), password.getPasswordNueva())) {
                user.get().setContrasena(bCryptPasswordEncoder.encode(password.getPasswordNueva()));
                usuarioService.saveEntity(user.get());
                attributes.addFlashAttribute("success", "Se ha cambiado la contraseña correctamente");
                return "redirect:/ajustes/password";
            } else if (bCryptPasswordEncoder.matches(password.getPasswordActual(), user.get().getPassword())) {
                model.addAttribute("failedIgual", "La nueva contraseña no puede ser igual a la actual");
                return "ajustesContrasena";
            } else {
                model.addAttribute("failedActual", "La contraseña actual es incorrecta");
                return "ajustesContrasena";
            }
        } else {
            model.addAttribute("failedNueva", "Las contraseñas no coinciden");
            return "ajustesContrasena";
        }

    }

    @GetMapping("/correo")
    public String verAjustesCorreo(Model model) {
        model.addAttribute("correo", new CorreoNuevo());
        return "ajustesCorreo";
    }

    @PostMapping("/correo")
    public String modificarCorreo(@Validated @ModelAttribute(value = "correo") CorreoNuevo correo,
                                  BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "ajustesCorreo";
        }
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));

        if (Objects.equals(user.get().getCorreo(), correo.getCorreoNuevo())) {
            model.addAttribute("failed", "El nuevo correo no puede ser el mismo que el actual");
            return "ajustesCorreo";

        } else if (usuarioService.getByCorreo(correo.getCorreoNuevo()).isPresent()) {
            model.addAttribute("failed", "El correo que intenta usar ya está en uso");
            return "ajustesCorreo";

        }
        if (bCryptPasswordEncoder.matches(correo.getPasswordActual(), user.get().getPassword())){
            user.get().setCorreo(correo.getCorreoNuevo());
            usuarioService.saveEntity(user.get());
            attributes.addFlashAttribute("success", "Se ha cambiado el correo correctamente");
            return "redirect:/ajustes/correo";
        }
        model.addAttribute("failedPassword","La contraseña no es correcta");
        return "ajustesCorreo";
    }

    @GetMapping("/fotoPerfil")
    public String verAjustesFotoPerfil(HttpSession session,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
            model.addAttribute("imagenRuta",user.get().getRutaImagen());

        return "ajustesFotoPerfil";
    }

    @PostMapping("/fotoPerfil")
    public String modificarFotoPerfil(@RequestParam("imagen") MultipartFile multipartFile,
                                     HttpSession session, RedirectAttributes attributes,Model model) {
        try{
            if (multipartFile.isEmpty()){
                model.addAttribute("failedImage","No has seleccionado ninguna imagen");
                return "ajustesFotoPerfil";
            }else{
                final String FILE_PATH_ROOT = "D:/ficheros";
                Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
                String nombreExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                if (nombreExtension.equals("png") || nombreExtension.equals("jpg")){
                    String nombreArchivo = "Usuario" + user.get().getId() + "ImagenPerfil." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                    if(!Objects.equals(user.get().getRutaImagen(), "/img/pacman.jpg")){
                        String nombreAntiguo = "Usuario" + user.get().getId() + "ImagenPerfil." + FilenameUtils.getExtension(user.get().getRutaImagen());
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ nombreAntiguo));
                    }
                    fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
                    user.get().setRutaImagen( "/imagenes/leerImagen/" + nombreArchivo);
                    session.setAttribute("rutaImagen", user.get().getRutaImagen());
                    usuarioService.saveEntity(user.get());
                    attributes.addFlashAttribute("success", "La imagen se ha cambiado correctamente");
                    return "redirect:/ajustes/fotoPerfil";
                }
                model.addAttribute("failedImage","No has seleccionado un formato correcto");
                return "ajustesFotoPerfil";

            }
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed","Hubo un error con la imagen");
            return "redirect:/ajustes/fotoPerfil";
        }

    }
}
