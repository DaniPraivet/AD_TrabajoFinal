DROP DATABASE IF EXISTS brawlhalla;

CREATE DATABASE brawlhalla CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;

USE brawlhalla;

--  TABLA: Armas

CREATE TABLE armas (
    id            INT UNSIGNED    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre_arma   VARCHAR(50)     NOT NULL
);


--  TABLA: Leyendas

CREATE TABLE leyendas (
    id          INT UNSIGNED    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(100)    NOT NULL,
    vida        INT             NOT NULL,
    fuerza      INT             NOT NULL,
    velocidad   INT             NOT NULL,
    destreza    INT             NOT NULL,
    defensa     INT             NOT NULL,
    id_arma1    INT UNSIGNED    NOT NULL,
    id_arma2    INT UNSIGNED    NOT NULL,
    FOREIGN KEY (id_arma1) REFERENCES armas(id),
    FOREIGN KEY (id_arma2) REFERENCES armas(id)
);


--  TABLA: Usuarios
CREATE TABLE usuarios (
    id          INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    usuario     VARCHAR(50)     NOT NULL,
    contrasena  VARCHAR(100)    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--  INSERTS: Armas
INSERT INTO armas (nombre_arma) VALUES
    ('Martillo'),
    ('Lanza'),
    ('Cañón'),
    ('Guadaña'),
    ('Pistolas'),
    ('Mandoble'),
    ('Chackram'),
    ('Espada'),
    ('Dagas'),
    ('Hacha'),
    ('Guanteletes'),
    ('Arco'),
    ('Lanza Cohete');


INSERT INTO leyendas (nombre, vida, fuerza, velocidad, destreza, defensa, id_arma1, id_arma2) VALUES
    ('Rayman', 6, 6, 7, 5, 4, 10, 11),
    ('Asuri',  6, 6, 7, 7, 2,  8,  9),
    ('Vector', 6, 3, 8, 7, 4, 12, 13);

--  INSERT: Usuario admin
INSERT INTO usuarios VALUES (1, 'admin', 'admin');
