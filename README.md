# Korner
Korner es una aplicación web que actúa como un repositorio multimedia, donde puedes guardar las películas, series, videojuegos, libros y animes que hayas visto y consultarlo en cualquier momento. Pero no solo eso, también puedes compartir estos elementos que has guardado con otros usuarios de la aplicación.

La aplicación se encuentra totalmente documentada y tiene generados javadocs, estos se enceuntran en la carpeta Docs.

Este proyecto ha sido realizado en conjunto con Álvaro Carrión. Github: carri1608 

# Funcionalidades
* Autenticación y creación de usuarios.
* Eliminar, habilitar e inhabilitar usuarios.
* Añadir, modificar y eliminar Películas, Animes, Series, Videojuegos, Libros, Plataformas de visualización, Géneros literarios y audiovisuales, Plataformas de videojuegos, Formatos de libros y Géneros de los usuarios.
* Buscar y filtrar usuarios, amigos, Películas, Animes, Series, Videojuegos y Libros.
* Añadir, bloquear y eliminar amigos.
* Compartir Películas, Animes, Series, Videojuegos y Libros con amigos.
* Ver las Películas, Animes, Series, Videojuegos y Libros compartidos.
* Generar y eliminar notificaciones.
* Enviar correos con una nueva contraseña generada aleatoriamente.



# Instalación
Para prodeceder a la instalación y ejecución de la aplicación debemos seguir los siguientes pasos:
- Instalar Docker
- Ejecutar el comando que hay en ele archivo docker-run.txt
- Arranacar el proyecto con el perfil desarrollo activado
- Lanzar la aplicación en la siguiente url: http://localhost:8080

# Guia Paso a Paso de Uso de la Aplicación
Para acceder a la aplicación se necesita tener una cuenta.
Para crear una cuenta, hay que rellenar correctamente todos los campos del formulario; no se pueden repetir correos ni nombres.
Si has olvidado tu contraseña, puedes pulsar en "¿Has olvidado tu contraseña?" e introducir el correo con el que te diste de alta en la aplicación. Si este es correcto y tu cuenta sigue activa, recibirás un correo en esa dirección con una nueva contraseña generada aleatoriamente, que podrás utilizar para acceder a tu cuenta; luego la podrás cambiar en ajustes.
Si no introduces el nombre de usuario y la contraseña correctos, no te dejará acceder a la aplicación. Una vez dentro, se muestra la página principal, desde la cual podemos navegar a las diferentes secciones de la aplicación.

En la sección de películas, podemos ver las películas que hemos guardado con sus datos. Podemos mostrar estas películas por orden alfabético ascendente o descendente, o por orden de agregación, tanto ascendente como descendente. También podemos buscar las películas por título o filtrarlas por género, año de visualización, puntuación o plataforma, y ordenar los resultados.

Podemos añadir una nueva película; para ello, es necesario rellenar los datos del formulario cumpliendo las validaciones. Al añadir una película, la imagen se guarda en el sistema de archivos. También podemos modificar los datos de una película ya guardada; si cambiamos la imagen, la anterior se elimina del sistema de archivos.

Además, podemos eliminar una película guardada; al hacerlo, la imagen también se elimina del sistema de archivos. Todos estos procesos los podemos realizar en las secciones de Animes, Libros, Series y Videojuegos.

Podemos compartir una película con otro usuario de la aplicación, pero para ello, el usuario debe ser amigo nuestro y no debe tenernos bloqueados. Para ver una película que han compartido con nosostros nos vamos a la sección de películas compartidas, podemos ver la película que nos han compartido, indicando el usuario que la compartió. También recibiremos una notificación. Si la película es modificada por su usuario, estos cambios también se reflejan aquí, y si se elimina, también desaparecerá de esta sección.

En la sección de amigos, podemos ver a los amigos que tenemos agregados, bloquearlos y eliminarlos. Conociendo el nombre de otro usuario de la aplicación, podemos buscarlo para enviarle una solicitud de amistad. Al enviar una solicitud, el otro usuario recibirá una notificación. Podemos ver las solicitudes que hemos recibido y las que hemos enviado, pero que aún no han sido aceptadas. También podemos rechazar las solicitudes de amistad o aceptarlas; si aceptas la solicitud, el usuario recibirá una notificación.

En la sección de ajustes, podemos cambiar nuestro nombre de usuario, contraseña, correo, imagen de perfil y seleccionar la página que queremos que se muestre al iniciar sesión. Si cambiamos el nombre o la imagen, estos cambios se reflejan en todas las partes de la aplicación donde aparecían.

Los administradores pueden realizar acciones que un usuario normal no puede. Pueden añadir, modificar o eliminar los géneros, plataformas, plataformas de videojuegos y formatos de los libros que aparecen como opciones en los formularios de añadir y modificar, así como los géneros del formulario de creación de cuenta. También pueden gestionar las cuentas de los usuarios, eliminándolas, desactivándolas o activándolas. Si la cuenta está eliminada o desactivada, el usuario no puede acceder a la aplicación con su cuenta. 

Creadencialies para acceder como adminstrador: 

Nombre -> admin 

Password-> password

Las notificaciones que recibes también se actualizan. Se muestran si el usuario del que proceden está habilitado; si tiene la cuenta desactivada, se ocultan, y si se elimina la cuenta, se eliminan. También se eliminan las notificaciones que indican que han compartido un elemento contigo si ese elemento ha sido eliminado.

