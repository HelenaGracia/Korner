INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1000","prueba1","2020","2","opinion de prueba");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1002","prueba2","2020","3","opinion de prueba2");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1003","prueba3","2020","4","opinion de prueba4");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1004","prueba4","2020","5","opinion de prueba3");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1005","prueba5","2020","2","opinion de prueba5");


INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("1","Romance");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("2","Acción");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("3","Fantasía");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("4","Terror");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("5","Animación");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("6","Comedia");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("7","Drama");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("8","Thriller");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("9","Aventura");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("10","Musical");
INSERT INTO generos_elementos_compartidos (id_genero_elemt_compart,nombre_genero) VALUES ("11","Documental");

INSERT INTO plataformas (id_plataforma, nombre_plataforma) VALUES ("1","Amazon");
INSERT INTO plataformas (id_plataforma, nombre_plataforma) VALUES ("2","Netflix");


INSERT INTO roles (id_rol,nombre_rol) VALUES ("1","ROLE_SUPERADMIN");
INSERT INTO roles (id_rol,nombre_rol) VALUES ("2","ROLE_ADMIN");
INSERT INTO roles (id_rol,nombre_rol) VALUES ("3","ROLE_USER");

INSERT INTO usuarios (id_usuario,nombre,anio_nacimiento,contrasena,id_roles) VALUES ("99","user","1998","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,anio_nacimiento,contrasena, id_roles) VALUES ("98","useradmin","1990","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","2");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("100","user1","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("101","user2","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("102","user3","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("103","user4","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("104","user5","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("105","user6","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("106","user7","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");

INSERT INTO amigos (id_amigos,id_amigo_origen,id_amigo_destino) VALUES ("100","101","102");
INSERT INTO amigos (id_amigos,id_amigo_origen,id_amigo_destino) VALUES ("101","101","103");
INSERT INTO amigos (id_amigos,id_amigo_origen,id_amigo_destino) VALUES ("102","101","104");
INSERT INTO amigos (id_amigos,id_amigo_origen,id_amigo_destino) VALUES ("103","101","105");
INSERT INTO amigos (id_amigos,id_amigo_origen,id_amigo_destino) VALUES ("104","101","106");


INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1006","99","Shoujo Kageki Revue Starlight the movie","2020","5","opinion de prueba","https://www.youtube.com/embed/AAabX4HO8xA?si=7bd5QYheVkxFF8aO");

INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1007","99","revue","2010","5","opinion de prueba","https://www.youtube.com/embed/AAabX4HO8xA?si=7bd5QYheVkxFF8aO");

INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1008","99","prueba6","2011","5","opinion de prueba","https://www.youtube.com/embed/AAabX4HO8xA?si=7bd5QYheVkxFF8aO");

INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1000","99","prueba1","2020","2","opinion de prueba","wwww.youtube");

INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1002","99","prueba2","2020","3","opinion de prueba2","wwww.youtube");

INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1003","98","prueba3","2020","4","opinion de prueba4", "wwww.youtube");

INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1004","98","prueba4","2020","5","opinion de prueba3","wwww.youtube");

INSERT INTO peliculas (id,id_pelicula_usuarios,titulo, year, puntuacion, opinion, trailer) VALUES ("1005","98","prueba5","2020","2","opinion de prueba5","wwww.youtube");


INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1000", "1");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1000", "2");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1006", "8");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1006", "9");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1006", "10");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1007", "10");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1008", "5");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1008", "4");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1002", "3");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1003", "4");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1003", "7");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1004", "9");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1005", "10");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1005", "11");
INSERT INTO pelicula_genero(id, id_generos_elemt_comp) VALUES ("1005", "6");

INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1000", "1");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1000", "2");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1006", "2");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1007", "2");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1008", "1");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1008", "2");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1002", "2");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1003", "1");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1004", "2");
INSERT INTO pelicula_plataforma(id,id_plataforma) VALUES ("1005", "1");
