DROP DATABASE IF EXISTS gamer_events;
CREATE DATABASE gamer_events
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gamer_events;

CREATE TABLE roles (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_roles_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios (
    id               BIGINT        NOT NULL AUTO_INCREMENT,
    username         VARCHAR(100)  NOT NULL,
    email            VARCHAR(150)  NOT NULL,
    password         VARCHAR(255)  NOT NULL,
    avatar_url       VARCHAR(500)  DEFAULT NULL,
    juegos_interes   VARCHAR(1000) DEFAULT NULL,
    notificar_email  BOOLEAN       NOT NULL DEFAULT FALSE,
    notificar_push   BOOLEAN       NOT NULL DEFAULT FALSE,
    fecha_registro   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuarios_username (username),
    UNIQUE KEY uk_usuarios_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios_roles (
    usuario_id  BIGINT NOT NULL,
    rol_id      BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_rol     FOREIGN KEY (rol_id)     REFERENCES roles(id)    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE eventos (
    id                     BIGINT        NOT NULL AUTO_INCREMENT,
    nombre                 VARCHAR(200)  NOT NULL,
    descripcion            TEXT          DEFAULT NULL,
    direccion              VARCHAR(300)  DEFAULT NULL,
    fecha_hora             DATETIME      DEFAULT NULL,
    organizadores          VARCHAR(300)  DEFAULT NULL,
    expositores_principales VARCHAR(500) DEFAULT NULL,
    categoria              VARCHAR(100)  DEFAULT NULL,
    tiene_estacionamiento  BOOLEAN       NOT NULL DEFAULT FALSE,
    tiene_locomocion       BOOLEAN       NOT NULL DEFAULT FALSE,
    tiene_comidas          BOOLEAN       NOT NULL DEFAULT FALSE,
    tiene_alojamiento      BOOLEAN       NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    KEY idx_eventos_categoria (categoria),
    KEY idx_eventos_fecha (fecha_hora),
    KEY idx_eventos_direccion (direccion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Roles
INSERT INTO roles (nombre) VALUES
('ROLE_USER'),
('ROLE_ADMIN');

-- Usuarios (contraseña: password123)
INSERT INTO usuarios (username, email, password, avatar_url, juegos_interes, notificar_email, notificar_push) VALUES
('gamer1', 'gamer1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://api.dicebear.com/7.x/bottts/svg?seed=gamer1', 'Valorant, League of Legends, CS2', TRUE,  FALSE),
('gamer2', 'gamer2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://api.dicebear.com/7.x/bottts/svg?seed=gamer2', 'Dota 2, CS2, Elden Ring',          FALSE, TRUE),
('gamer3', 'gamer3@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'https://api.dicebear.com/7.x/bottts/svg?seed=gamer3', 'Minecraft, Fortnite, Apex Legends', TRUE,  TRUE);

-- Roles de usuarios
INSERT INTO usuarios_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u
JOIN roles r ON r.nombre = 'ROLE_USER'
WHERE u.username IN ('gamer1', 'gamer2', 'gamer3');

INSERT INTO usuarios_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u
JOIN roles r ON r.nombre = 'ROLE_ADMIN'
WHERE u.username = 'gamer3';

-- Eventos
INSERT INTO eventos
(nombre, descripcion, direccion, fecha_hora, organizadores, expositores_principales, categoria,
 tiene_estacionamiento, tiene_locomocion, tiene_comidas, tiene_alojamiento)
VALUES
('Torneo Nacional Valorant 2026',
 'Competencia de equipos amateur y profesionales de Valorant. Premiación en efectivo y clasificación a torneo internacional.',
 'Centro de Convenciones, Santiago',
 '2026-12-15 10:00:00',
 'GamerChile Esports',
 'KRÜ Esports, Leviatán, Furia',
 'Esports',
 TRUE, TRUE, TRUE, FALSE),

('Expo Gaming 2026',
 'Feria con stands de marcas, torneos abiertos, cosplay, charlas de desarrolladores y zona retro.',
 'Movistar Arena, Santiago',
 '2026-11-20 09:00:00',
 'ExpoGamer Chile',
 'Nintendo, PlayStation, Xbox, Riot Games',
 'Expo',
 TRUE, TRUE, TRUE, TRUE),

('LAN Party Retro',
 'Evento nostálgico con clásicos de los 90 y 2000. Trae tu PC o consola y comparte con la comunidad.',
 'Espacio Riesco, Santiago',
 '2026-10-05 18:00:00',
 'RetroGamers CL',
 'Speedrunners invitados, Coleccionistas',
 'Comunidad',
 FALSE, TRUE, TRUE, FALSE),

('Torneo CS2 Universitario',
 'Competencia entre universidades de la región metropolitana. Inscripciones abiertas por equipo.',
 'Universidad Duoc UC, San Carlos de Apoquindo',
 '2026-09-28 14:00:00',
 'Duoc Esports',
 'Comentaristas profesionales invitados',
 'Esports',
 TRUE, TRUE, FALSE, FALSE),

('Meetup Desarrolladores de Videojuegos',
 'Encuentro de indie developers, showcases de prototipos y networking con publishers.',
 'Hub Providencia, Santiago',
 '2026-11-08 17:30:00',
 'IGDA Chile',
 'Estudios indie nacionales',
 'Desarrollo',
 FALSE, TRUE, TRUE, FALSE),

('Apex Legends Community Cup',
 'Torneo abierto para la comunidad de Apex Legends. Formato battle royale con 20 escuadrones.',
 'Online + Streaming desde Arena Esports',
 '2026-10-22 20:00:00',
 'Apex CL Community',
 'Streamers invitados',
 'Esports',
 FALSE, FALSE, FALSE, FALSE);