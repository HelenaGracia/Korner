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

    /**
     * ajustes Es la pagina general de ajustes de la web, que enlaza con las otras opciones de ajustes.
     *
     * @param session Lo usamos para traernos con nosotros el nombre y la foto de perfil del usuario que se han guarado previamente al hacer el login.
     * @param model Utilizamos el model para proporcionar los datos recogidos de la sesión al html ajustes.
     * @return Se vuelve al html de ajustes.
     */

    @GetMapping
    public String ajustes(HttpSession session,Model model) {
        //recogemos la ruta de imagen guarada en la session al hacer login, y lo pasamos al html mediante imagenUsuario
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        //recogemos el nombre del usuario en la session al hacer login, y lo pasamos al html mediante nameUsuario
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        return "ajustes";
    }

    /**
     * verAjustesCambioNombre Es la página de ajustes que nos permite cambiar de nombre de usuario.
     *
     * @param model Lo usamos para traernos con nosotros el id del usuario que usa la aplicación, asi como su nombre y su foto de perfil.
     * @param session Lo usamos para traernos con nosotros el id del usuario que usa la aplicación, asi como su nombre y su foto de perfil.
     * @return Se vuelve al html de los ajustes de cambio de nombre.
     */

    @GetMapping("/nombre")
    public String verAjustesCambioNombre(Model model,HttpSession session){
        model.addAttribute("nombre",new NombreNuevo());
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        return "ajustesNombre";
    }

    /**
     * modificarCambioNombre se encarga de comprobar que el usuario haya proporcionado correctamente el nombre de usuario que hay en base de datos y
     * que la contraseña nueva no sea igual que la contraseña en base de datos. Si todos los datos son correctos se devuelve a la pagina de
     * cambiar contraseña con la contraseña cambiada en la base de datos.
     *
     * @param nombre Es el objeto con los datos de los campos de la entidad NombreNuevo con los datos proporcionados por el usuario.
     * @param bindingResult Comprueba que no haya datos incorrectos de los datos proporcionados con las restricciones de la entidad.
     * Valida los datos de nombre de la entidad NombreNuevo.
     * @param session Lo usamos para traernos con nosotros el id del usuario que usa la aplicación, asi como su nombre y su foto de perfil.
     * @param attributes Utilizamos el attributes para redireccionar con mensajes explicativos de errores o exito.
     * @param model Utilizamos el modelo para volver al html con mensajes explicativos de errores o exito.
     */

    @PostMapping("/nombre")
    public String modificarCambioNombre(@Validated @ModelAttribute(value = "nombre") NombreNuevo nombre, BindingResult bindingResult,
                                        HttpSession session, RedirectAttributes attributes, Model model){
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        //Comprueba si hay errores con las validaciones de la entidad NombreNuevo
        if (bindingResult.hasErrors()) {
            return "ajustesNombre";
        }
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
        //Comprueba que el nombre proporcionado por el usuario no es el mismo que el nombre que ya tiene en base de datos
        if (Objects.equals(user.get().getNombre(), nombre.getNombre())) {
            model.addAttribute("failed", "El nuevo nombre no puede ser el mismo que el actual");
            return "ajustesNombre";
        }
        //Comprueba que el nombre proporcionado por el usuario no exista ya en base de datos
        else if(usuarioService.getByName(nombre.getNombre()).isPresent()){
            model.addAttribute("failed","El nombre introducido ya existe");
            return "ajustesNombre";
        }
        //Comprueba que la contraseña proporcionada por el usuario es igual que la que hay en base de datos
        if (bCryptPasswordEncoder.matches(nombre.getPasswordActual(), user.get().getPassword())){
            //Cambia el nombre del usuario por el nuevo nombre que ha elegido
            user.get().setNombre(nombre.getNombre());
            //Hacemos el cambio en BBDD
            usuarioService.saveEntity(user.get());
            //Se cambia en la session guardada el nuevo nombre
            session.setAttribute("userName", user.get().getNombre());
            attributes.addFlashAttribute("success", "Se ha cambiado el nombre correctamente");
            return "redirect:/ajustes/nombre";
        }
        //Si la contraseña no es correcta se devuelve a la vista con el mensaje explicativo
        model.addAttribute("failedPassword","La contraseña no es correcta");
        return "ajustesNombre";
    }

    /**
     * verAjustesInicioSesion Metódo el cual obtiene una pagina en la cual hay un formulario para cambiar la pagina principal que
     * visualizas cuando inicias sesion a traves del login de la web
     *
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario, donde se almacenan atributos
     * la imagen de perfil, y el nombre de usuario
     * @return String del nombre de la vista que debe ser renderizada, ajustesInicioSesion es el html
     * para cambiar la configuracion de inicio de sesion
     */

    @GetMapping("/inicioSesion")
    public String verAjustesInicioSesion(Model model, HttpSession session) {
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        return "ajustesInicioSesion";
    }

    /**
     * modificarInicioSesion Metodo para cambiar el string asociado al campo AjustesInicioSesion en la entidad del usuario,
     * primero se comprueba si ha elegido la misma configuración que ya tenia y si es asi se le retorna con un mensaje  de fallo,
     * si elige un diferente, se cambia en BBDD, se guarda y se redirije con un mensaje de exito
     *
     * @param ajustes String que proviene del usuario a traves de un formulario
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * que está cambiando su ajustes de inicio de sesion
     * @param attributes permite añadir atributos que se envían como parte de una redirección, en este caso el mensaje de exíto
     * @param model permite añadir atributos que se envian como parte de un return, en este caso el mensaje de error
     * @return En caso de fallo, return a ajustesInicioSesion, en caso de exito redirección al endpoint /ajustes/inicioSesion
     */

    @PostMapping("/inicioSesion")
    public String modificarInicoSesion(@RequestParam(value = "ajustes") String ajustes,
                                     HttpSession session, RedirectAttributes attributes,Model model) {
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        if(Objects.equals(user.get().getAjustesInicioSesion(), ajustes)){
            model.addAttribute("failed","Ya tiene la configuracion en: "+ajustes);
            return "ajustesInicioSesion";
        }
        user.get().setAjustesInicioSesion(ajustes);
        usuarioService.saveEntity(user.get());
        attributes.addFlashAttribute("success","Se ha cambiado correctamente a " + ajustes);

        return "redirect:/ajustes/inicioSesion";
    }

    /**
     * verAjustesPassword Metodo que nos permite obtener el html de ajustesContrasena para poder cambiar la contraseña del usuario
     *
     * @param model se utiliza para pasar datos desde el controlador a la vista, en este caso tambien se inicia un objeto new PasswordNueva
     * para poder cambiar la contraseña del usuario con las validaciones correspondientes
     * @param session permite acceder a la sesión actual del usuario donde se almacena la imagen de perfil, y el nombre de usuario
     * @return Se retorna a ajustesContrasena html para cambiar la contraseña del usuario
     */

    @GetMapping("/password")
    public String verAjustesPassword(Model model,HttpSession session) {
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        model.addAttribute("password", new PasswordNueva());
        return "ajustesContrasena";
    }

    /**
     * modificarPassword se encarga de comprobar que el usuario haya proporcionado correctamente la contraseña que hay en base de datos y
     * que la contraseña nueva no sea igual que la contraseña en base de datos. Si todos los datos son correctos se devuelve a la pagina de
     * cambiar contraseña con la contraseña cambiada en la base de datos.
     *
     * @param password Es el objeto con los datos de los campos de la entidad PasswordNueva con los datos proporcionados por el usuario.
     * @param bindingResult Comprueba que no haya datos incorrectos de los datos proporcionados con las restricciones de la entidad.
     * Valida los datos de password de la entidad PasswordNueva.
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * que está cambiando su ajustes de inicio de sesion
     * @param attributes Utilizamos el attributes para redireccionar con mensajes explicativos de errores o exito.
     * @param model Utilizamos el modelo para volver al html con mensajes explicativos de errores o exito.
     * @return se retorna al html ajustesContrasena o se redirije al endpoint /ajustes/password
     */

    @PostMapping("/password")
    public String modificarPassword(@Validated @ModelAttribute(value = "password") PasswordNueva password, BindingResult bindingResult,
                                    HttpSession session, RedirectAttributes attributes, Model model) {
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());

        //Comprueba si hay errores con las validaciones de la entidad PasswordNueva
        if (bindingResult.hasErrors()) {
            return "ajustesContrasena";
        }
        //Recogemos el usuario que va cambiar la contraseña
        Optional<Usuario> user = usuarioService.getById(Integer.valueOf((session.getAttribute("idusuario").toString())));
        //Comprobamos que la contraseñaNueva sea igual a la contraseñaNueva2 de doble validación dada en el html
        if (Objects.equals(password.getPasswordNueva(), password.getPasswordNueva2())) {
            //Comprueba que la contraseña dada en el html coincida con la contraseña en base de datos del usuario
            if (bCryptPasswordEncoder.matches(password.getPasswordActual(), user.get().getPassword()) && !Objects.equals(password.getPasswordActual(), password.getPasswordNueva())) {
                //Cambiamos la contraseña del usuario por la nueva contraseña
                user.get().setContrasena(bCryptPasswordEncoder.encode(password.getPasswordNueva()));
                //Guardamos en base de datos el usuario con el cambio realizado
                usuarioService.saveEntity(user.get());
                //Redireccionamos a la vista de ajustes, cambio de contraseña con el mensaje de exito
                attributes.addFlashAttribute("success", "Se ha cambiado la contraseña correctamente");
                return "redirect:/ajustes/password";
            }
            //Comprueba que la nueva contraseña dada no sea igual a la anterior que hay en base de datos
            else if (bCryptPasswordEncoder.matches(password.getPasswordActual(), user.get().getPassword())) {
                //Se devuelve a la vista con el fallo explicativo
                model.addAttribute("failedIgual", "La nueva contraseña no puede ser igual a la actual");
                return "ajustesContrasena";
            } else {
                //Si no coincide la contraseña dada por el usuario con la que esta en base de datos, se devuelve a la vista con el mensaje explicativo
                model.addAttribute("failedActual", "La contraseña actual es incorrecta");
                return "ajustesContrasena";
            }
        } else {
            model.addAttribute("failedNueva", "Las contraseñas no coinciden");
            return "ajustesContrasena";
        }

    }

    /**
     * verAjustesCorreo  Metodo que nos permite obtener el html de ajustesCorreo para poder cambiar el correo del usuario
     *
     * @param model se utiliza para pasar datos desde el controlador a la vista, en este caso tambien se inicia un objeto new CorreoNuevo
     * para poder cambiar el correo del usuario con las validaciones correspondientes
     * @param session permite acceder a la sesión actual del usuario donde se almacena la imagen de perfil, y el nombre de usuario
     * @return Se retorna a ajustesCorreo html para cambiar el correo del usuario
     */

    @GetMapping("/correo")
    public String verAjustesCorreo(Model model,HttpSession session) {
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        model.addAttribute("correo", new CorreoNuevo());
        return "ajustesCorreo";
    }

    /**
     * modificarCorreo se encarga de comprobar que el usuario haya proporcionado correctamente el correo que hay en base de datos y
     * que el correo nuevo no sea igual que el correo en base de datos. Si todos los datos son correctos se devuelve a la pagina de
     * cambiar correo con el correo cambiado en la base de datos.
     *
     * @param correo Es el objeto con los datos de los campos de la entidad CorreoNuevo con los datos proporcionados por el usuario.
     * @param bindingResult Comprueba que no haya datos incorrectos de los datos proporcionados con las restricciones de la entidad.
     * Valida los datos de password de la entidad CorreoNuevo.
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * que está cambiando su ajustes de inicio de sesion
     * @param model Utilizamos el modelo para volver al html con mensajes explicativos de errores o exito.
     * @param attributes Utilizamos el attributes para redireccionar con mensajes explicativos de errores o exito.
     * @return al html de ajustesCorreo o redirect al endpoint: /ajustes/correo
     */

    @PostMapping("/correo")
    public String modificarCorreo(@Validated @ModelAttribute(value = "correo") CorreoNuevo correo,
                                  BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes attributes) {
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
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

    /**
     * verAjustesFotoPerfil Metodo que nos permite obtener el html de ajustesFotoPerfil para poder cambiar la foto del usuario
     *
     * @param model se utiliza para pasar datos desde el controlador a la vista
     * @param session permite acceder a la sesión actual del usuario donde se almacena la imagen de perfil, y el nombre de usuario
     * @return Se retorna a ajustesFotoPerfil html para cambiar la foto de perfil del usuario
     */

    @GetMapping("/fotoPerfil")
    public String verAjustesFotoPerfil(HttpSession session,Model model) {
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
        return "ajustesFotoPerfil";
    }

    /**
     * modificarFotoPerfil se encarga de comprobar que el usuario haya proporcionado correctamente la imagen para su foto de perfil.
     * Primero se comprueba que haya enviado una imagen, despues se comprueba que tenga una extension valida, png o jpg.
     * Si lo anterior es correcto, se comprueba si tiene en la BBDD una ruta de imagen propia o tiene la foto de perfil por defecto,
     * si tiene una propia se elimina el archivo y se procede a guardar la nueva imagen con la ruta de imagen correspondientes
     *
     * @param multipartFile En el multipartFile recogemos la imagen que nos ha enviado el usuario, que en este caso es para
     * cambiar su foto de perfil, la extension del archivo de la imagen debe ser png o jpg
     * @param session Permite acceder a la sesión actual del usuario, en la que se almacena información sobre el usuario
     * que está cambiando su ajustes de inicio de sesion
     * @param model Utilizamos el modelo para volver al html con mensajes explicativos de errores o exito.
     * @param attributes Utilizamos el attributes para redireccionar con mensajes explicativos de errores o exito.
     * @return al html de ajustesFotoPerfil o redirect al endpoint: /ajustes/fotoPerfil
     */

    @PostMapping("/fotoPerfil")
    public String modificarFotoPerfil(@RequestParam("imagen") MultipartFile multipartFile,
                                     HttpSession session, RedirectAttributes attributes,Model model) {
        model.addAttribute("imagenUsuario",session.getAttribute("rutaImagen").toString());
        model.addAttribute("nameUsuario",session.getAttribute("userName").toString());
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
                    if(!Objects.equals(user.get().getRutaImagen(), "/img/icon1.png")){
                        String nombreAntiguo = "Usuario" + user.get().getId() + "ImagenPerfil." + FilenameUtils.getExtension(user.get().getRutaImagen());
                        FileUtils.delete(new File(FILE_PATH_ROOT+ "/"+ nombreAntiguo));
                    }
                    fileSystemStorageService.storeWithName(multipartFile, nombreArchivo);
                    user.get().setRutaImagen( "/imagenes/leerImagen/" + nombreArchivo);
                    usuarioService.saveEntity(user.get());
                    session.setAttribute("rutaImagen", user.get().getRutaImagen());
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
