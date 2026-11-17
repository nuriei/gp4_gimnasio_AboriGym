-- ============================================================
-- ABORIGYM - BASE DE DATOS COMPLETA PARA DESARROLLO Y PRUEBAS
-- Base: gp4_gimnasio_aborigym
-- Compatible con el código actual que consulta cantidad_inicial.
--
-- INSTRUCCIONES:
-- 1) Importar este archivo desde phpMyAdmin.
-- 2) ATENCION: este script elimina y vuelve a crear la base indicada.
--    Se perderán los datos que existan actualmente en esa base.
-- 3) Todos los integrantes deben importar este mismo archivo.
-- ============================================================

-- Primero crear manualmente en phpMyAdmin una base llamada:
-- gp4_gimnasio_aborigym
USE gp4_gimnasio_aborigym;

-- ------------------------------------------------------------
-- TABLAS
-- ------------------------------------------------------------

CREATE TABLE socios (
    id_socio INT NOT NULL AUTO_INCREMENT,
    dni VARCHAR(20) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    edad INT NOT NULL,
    correo VARCHAR(100) NOT NULL,
    telefono VARCHAR(50) DEFAULT NULL,
    estado TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_socio),
    UNIQUE KEY uk_socios_dni (dni),
    UNIQUE KEY uk_socios_correo (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

CREATE TABLE entrenadores (
    id_entrenador INT NOT NULL AUTO_INCREMENT,
    dni VARCHAR(20) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    estado TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_entrenador),
    UNIQUE KEY uk_entrenadores_dni (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

CREATE TABLE clases (
    id_clase INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    id_entrenador INT NOT NULL,
    horario TIME NOT NULL,
    capacidad INT NOT NULL,
    estado TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_clase),
    KEY idx_clases_entrenador (id_entrenador),
    CONSTRAINT fk_clase_entrenador
      FOREIGN KEY (id_entrenador) REFERENCES entrenadores (id_entrenador)
      ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

CREATE TABLE pases (
    id_Pase INT NOT NULL AUTO_INCREMENT,
    id_Socio INT NOT NULL,
    cantidadEntradas INT NOT NULL,
    cantidad_inicial INT NOT NULL,
    fecha_Inicio DATE NOT NULL,
    fecha_Fin DATE NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    estado TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_Pase),
    KEY idx_pases_socio (id_Socio),
    CONSTRAINT fk_membresia_socio
      FOREIGN KEY (id_Socio) REFERENCES socios (id_socio)
      ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

CREATE TABLE asistencias (
    id_asistencia INT NOT NULL AUTO_INCREMENT,
    id_socio INT NOT NULL,
    id_clase INT NOT NULL,
    fecha_asistencia DATE NOT NULL,
    PRIMARY KEY (id_asistencia),
    KEY idx_asistencias_socio (id_socio),
    KEY idx_asistencias_clase (id_clase),
    CONSTRAINT fk_asistencia_clase
      FOREIGN KEY (id_clase) REFERENCES clases (id_clase)
      ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_asistencia_socio
      FOREIGN KEY (id_socio) REFERENCES socios (id_socio)
      ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

-- ------------------------------------------------------------
-- ENTRENADORES
-- Se conservan los registros del volcado anterior y se agregan
-- entrenadores de prueba.
-- ------------------------------------------------------------

INSERT INTO entrenadores
(id_entrenador, dni, nombre, apellido, especialidad, estado) VALUES
(2, '31265077', 'Rosario', 'Ibañez', 'YOGA', 1),
(4, '33816309', 'Nuri', 'Ibañez', 'CROSSFIT', 0),
(5, '47846957', 'Juan', 'Figueroa', 'MUSCULACION', 1),
(6, '24587965', 'Sandra', 'Carreño', 'PILATES', 1),
(7, '45879684', 'Marcelo', 'Quiroga', 'FUNCIONAL', 0),
(1001, '40100101', 'Lucía', 'Fernández', 'MUSCULACION', 1),
(1002, '40100102', 'Marcos', 'Pereyra', 'CROSSFIT', 1),
(1003, '40100103', 'Sofía', 'Gómez', 'PILATES', 1),
(1004, '40100104', 'Diego', 'Luna', 'SPINNING', 1),
(1005, '40100105', 'Carolina', 'Ríos', 'YOGA', 1),
(1006, '40100106', 'Paula', 'Méndez', 'PILATES', 1),
(1007, '40100107', 'Gabriel', 'Ruiz', 'FUNCIONAL', 1);

-- ------------------------------------------------------------
-- SOCIOS
-- Se conservan los cuatro socios del volcado anterior.
-- Hay personas con igual nombre y apellido, pero DNI, correo
-- e ID diferentes para probar que la aplicación no las confunda.
-- ------------------------------------------------------------

INSERT INTO socios
(id_socio, dni, nombre, apellido, edad, correo, telefono, estado) VALUES
(1, '93863238', 'Davicito', 'Verita', 28, 'davidverabarz2017@email.com', '2236789056', 1),
(2, '33089698', 'Nurita', 'Ibaniez', 28, 'nuryibaniez39@email.com', '2664295549', 1),
(3, '3333333', 'Nuri', 'Ibalez', 35, 'nuriei@hotmail.com', '157896587', 0),
(4, '25987632', 'Marta', 'Saenz', 25, 'marta@hotmail.com', '15408597', 1),
(1001, '42100101', 'Camila', 'Torres', 27, 'camila.torres1001@test.com', '2664001001', 1),
(1002, '42100102', 'Mateo', 'Sosa', 32, 'mateo.sosa1002@test.com', '2664001002', 1),
(1003, '42100103', 'Valentina', 'Díaz', 24, 'valentina.diaz1003@test.com', '2664001003', 1),
(1004, '42100104', 'Tomás', 'Castro', 38, 'tomas.castro1004@test.com', '2664001004', 1),
(1005, '42100105', 'Julieta', 'Moreno', 29, 'julieta.moreno1005@test.com', '2664001005', 1),
(1006, '42100106', 'Bruno', 'Herrera', 41, 'bruno.herrera1006@test.com', '2664001006', 1),
(1007, '42100107', 'Milagros', 'Vega', 22, 'milagros.vega1007@test.com', '2664001007', 0),
(1008, '42100108', 'Facundo', 'Acosta', 35, 'facundo.acosta1008@test.com', '2664001008', 1),
(1009, '42100109', 'Ana', 'Pérez', 30, 'ana.perez1009@test.com', '2664001009', 1),
(1010, '42100110', 'Ana', 'Pérez', 26, 'ana.perez1010@test.com', '2664001010', 1),
(1011, '42100111', 'Juan', 'García', 34, 'juan.garcia1011@test.com', '2664001011', 1),
(1012, '42100112', 'Juan', 'García', 39, 'juan.garcia1012@test.com', '2664001012', 1),
(1013, '42100113', 'Laura', 'López', 28, 'laura.lopez1013@test.com', '2664001013', 1),
(1014, '42100114', 'Laura', 'López', 31, 'laura.lopez1014@test.com', '2664001014', 1);

-- ------------------------------------------------------------
-- CLASES
-- Casos de prueba:
-- - Un entrenador dicta más de una disciplina en horarios distintos.
-- - Pilates se dicta a las 10:00 por dos entrenadoras diferentes.
-- - No se asignan dos clases simultáneas al mismo entrenador.
-- ------------------------------------------------------------

INSERT INTO clases
(id_clase, nombre, id_entrenador, horario, capacidad, estado) VALUES
(1001, 'Musculación', 1001, '08:00:00', 15, 1),
(1002, 'Crossfit', 1002, '09:00:00', 12, 1),
(1003, 'Pilates', 1003, '10:00:00', 10, 1),
(1004, 'Spinning', 1004, '18:00:00', 16, 1),
(1005, 'Yoga', 2, '19:00:00', 12, 1),
(1006, 'Funcional', 1001, '20:00:00', 14, 1),
(1007, 'Zumba', 1005, '17:00:00', 20, 1),
(1008, 'Pilates', 1006, '10:00:00', 8, 1),
(1009, 'Yoga', 1005, '08:00:00', 12, 1),
(1010, 'Funcional', 1007, '18:00:00', 14, 1),
(1011, 'Musculación', 5, '09:00:00', 18, 1),
(1012, 'Crossfit (inactiva)', 4, '11:00:00', 10, 0),
(1013, 'Spinning', 1004, '19:30:00', 16, 1);

-- ------------------------------------------------------------
-- PASES
-- cantidad_inicial = paquete contratado originalmente.
-- cantidadEntradas = saldo disponible actual.
-- Los saldos consideran las asistencias de ejemplo cargadas abajo.
-- ------------------------------------------------------------

INSERT INTO pases
(id_Pase, id_Socio, cantidadEntradas, cantidad_inicial,
 fecha_Inicio, fecha_Fin, costo, estado) VALUES
(1001, 1001, 10, 12, DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 25 DAY), 18000.00, 1),
(1002, 1002, 7, 8, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 12000.00, 1),
(1003, 1003, 5, 8, DATE_SUB(CURDATE(), INTERVAL 45 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY), 12000.00, 1),
(1004, 1004, 12, 12, DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 35 DAY), 18000.00, 1),
(1005, 1005, 8, 8, DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 27 DAY), 12000.00, 0),
(1006, 1006, 0, 8, DATE_SUB(CURDATE(), INTERVAL 8 DAY), DATE_ADD(CURDATE(), INTERVAL 22 DAY), 12000.00, 1),
(1007, 1008, 19, 20, DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 29 DAY), 25000.00, 1),
(1008, 1009, 6, 8, DATE_SUB(CURDATE(), INTERVAL 4 DAY), DATE_ADD(CURDATE(), INTERVAL 26 DAY), 12000.00, 1),
(1009, 1010, 7, 8, DATE_SUB(CURDATE(), INTERVAL 2 DAY), DATE_ADD(CURDATE(), INTERVAL 28 DAY), 12000.00, 1),
(1010, 1011, 11, 12, DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 29 DAY), 18000.00, 1);

-- ------------------------------------------------------------
-- ASISTENCIAS DE EJEMPLO
-- Se usan fechas recientes para que aparezcan en los historiales.
-- ------------------------------------------------------------

INSERT INTO asistencias
(id_asistencia, id_socio, id_clase, fecha_asistencia) VALUES
(1001, 1001, 1001, DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
(1002, 1001, 1002, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
(1003, 1002, 1004, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
(1004, 1008, 1003, CURDATE()),
(1005, 1009, 1008, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
(1006, 1009, 1007, CURDATE()),
(1007, 1010, 1003, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
(1008, 1011, 1011, CURDATE());

-- Ajustar AUTO_INCREMENT para que los próximos registros sigan después
-- de los IDs de prueba.
ALTER TABLE socios AUTO_INCREMENT = 1100;
ALTER TABLE entrenadores AUTO_INCREMENT = 1100;
ALTER TABLE clases AUTO_INCREMENT = 1100;
ALTER TABLE pases AUTO_INCREMENT = 1100;
ALTER TABLE asistencias AUTO_INCREMENT = 1100;

-- ------------------------------------------------------------
-- CONSULTAS DE CONTROL
-- ------------------------------------------------------------

SELECT * FROM socios ORDER BY id_socio;
SELECT * FROM entrenadores ORDER BY id_entrenador;
SELECT * FROM clases ORDER BY id_clase;
SELECT * FROM pases ORDER BY id_Pase;
SELECT * FROM asistencias ORDER BY id_asistencia;

-- Verificar que cantidad_inicial existe y comparar saldo vs. paquete.
SELECT id_Pase, id_Socio, cantidad_inicial, cantidadEntradas,
       (cantidad_inicial - cantidadEntradas) AS entradas_consumidas
FROM pases
ORDER BY id_Pase;
