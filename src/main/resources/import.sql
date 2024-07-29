INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1000","prueba1","2020","2","opinion de prueba");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1002","prueba2","2020","3","opinion de prueba2");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1003","prueba3","2020","4","opinion de prueba4");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1004","prueba4","2020","5","opinion de prueba3");
INSERT INTO animes (id,titulo, year, puntuacion, opinion) VALUES ("1005","prueba5","2020","2","opinion de prueba5");


INSERT INTO roles (id_rol,nombre_rol) VALUES ("1","ROLE_SUPERADMIN");
INSERT INTO roles (id_rol,nombre_rol) VALUES ("2","ROLE_ADMIN");
INSERT INTO roles (id_rol,nombre_rol) VALUES ("3","ROLE_USER");

INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("99","user","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","3");
INSERT INTO usuarios (id_usuario,nombre,contrasena, id_roles) VALUES ("98","useradmin","$2a$12$m8Z4FDXlPZtkX59jm0moieaj5hlPnLPvCahm.Xxd2PJrGWQNArFte","2");
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

