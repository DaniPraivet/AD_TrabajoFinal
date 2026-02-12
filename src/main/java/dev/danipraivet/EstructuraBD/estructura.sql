drop database if exists instituto;

create database instituto character set latin1 collate latin1_spanish_Ci;

use instituto;

create table alumno(
id int unsigned not null primary key auto_increment,
nombre varchar(100),
carnet_conducir tinyint,
estado_matricula varchar(50),
direccion varchar(200)
);

create table asignatura(
id int unsigned not null primary key auto_increment,
nombre varchar(100),
curso smallint unsigned
);


CREATE TABLE matricula (
id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
id_alumno INT UNSIGNED,
id_asignatura INT UNSIGNED,
nota DOUBLE,
FOREIGN KEY (id_alumno) REFERENCES alumno(id),
FOREIGN KEY (id_asignatura) REFERENCES asignatura(id)
);


-- INSERTS

-- Inserts para la tabla alumno
INSERT INTO alumno (nombre, carnet_conducir, estado_matricula, direccion) VALUES
('Juan Pérez', 1, 'Matriculado', 'Calle Falsa 123, Madrid'),
('Lucía Gómez', 0, 'Pendiente', 'Av. del Sol 45, Valencia'),
('Carlos Ruiz', 1, 'Matriculado', 'Calle Luna 78, Barcelona'),
('María López', 1, 'Matriculado', 'Paseo del Río 22, Sevilla'),
('Jorge Fernández', 0, 'Baja', 'Calle Jardín 15, Bilbao'),
('Elena Martínez', 1, 'Matriculado', 'Av. del Mar 89, Málaga'),
('Andrés Torres', 0, 'Pendiente', 'Calle Norte 101, Zaragoza'),
('Sandra Díaz', 1, 'Matriculado', 'Camino Real 33, Granada'),
('Pablo Navarro', 0, 'Baja', 'Calle Sur 66, Valladolid'),
('Natalia Romero', 1, 'Matriculado', 'Plaza Mayor 9, Alicante');

-- Inserts para la tabla asignatura
INSERT INTO asignatura (nombre, curso) VALUES
('Matemáticas I', 1),
('Lengua Española', 1),
('Historia Universal', 2),
('Física', 2),
('Química', 2),
('Inglés Avanzado', 3),
('Programación', 3),
('Biología', 1),
('Educación Física', 1),
('Filosofía', 2);


-- Inserts para la tabla matricula
INSERT INTO matricula (id_alumno, id_asignatura, nota) VALUES
(1, 1, 8.5), (1, 2, 7.0), (1, 3, 6.5), (1, 4, 9.0), (1, 5, 7.8),
(2, 1, 5.5), (2, 6, 8.2), (2, 7, 6.9), (2, 8, 7.3), (2, 9, 9.1),
(3, 2, 7.5), (3, 3, 6.0), (3, 4, 8.0), (3, 5, 5.9), (3, 10, 8.7),
(4, 1, 9.2), (4, 5, 7.5), (4, 6, 6.4), (4, 8, 8.1), (4, 10, 9.0),
(5, 2, 6.8), (5, 3, 5.5), (5, 7, 7.6), (5, 8, 6.0), (5, 9, 8.3),
(6, 4, 7.7), (6, 5, 6.9), (6, 6, 9.0), (6, 9, 8.5), (6, 10, 7.1),
(7, 1, 6.2), (7, 3, 7.4), (7, 4, 5.6), (7, 6, 6.7), (7, 8, 8.8),
(8, 2, 8.0), (8, 4, 7.2), (8, 5, 6.1), (8, 9, 7.9), (8, 10, 8.4),
(9, 3, 6.3), (9, 5, 8.6), (9, 6, 7.0), (9, 7, 7.3), (9, 10, 9.5),
(10, 1, 5.8), (10, 2, 7.9), (10, 8, 6.6), (10, 9, 8.2), (10, 10, 7.7);

CREATE TABLE `usuarios` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `usuario` varchar(50) COLLATE latin1_spanish_ci NOT NULL,
  `contrasena` varchar(100) COLLATE latin1_spanish_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

INSERT INTO `usuarios` VALUES (1,'admin','admin');