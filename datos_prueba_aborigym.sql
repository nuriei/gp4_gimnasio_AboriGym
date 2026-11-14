-- Datos de prueba para AboriGym
-- Compatible con el esquema del ZIP gp4_gimnasio_AboriGym(3).zip
-- IMPORTANTE: ejecutar sobre la base gp4_gimnasio_aborigym ya creada.
-- Los ID comienzan en 1001 para reducir el riesgo de chocar con registros existentes.
-- Las fechas de los pases se calculan respecto de CURDATE() para que las pruebas sigan vigentes.

USE gp4_gimnasio_aborigym;

START TRANSACTION;

-- ENTRENADORES
INSERT INTO entrenadores (id_entrenador, dni, nombre, apellido, especialidad, estado) VALUES
(1001, '40100101', 'Lucía', 'Fernández', 'MUSCULACION', 1),
(1002, '40100102', 'Marcos', 'Pereyra', 'CROSSFIT', 1),
(1003, '40100103', 'Sofía', 'Gómez', 'PILATES', 1),
(1004, '40100104', 'Diego', 'Luna', 'SPINNING', 1),
(1005, '40100105', 'Carolina', 'Ríos', 'YOGA', 0);

-- SOCIOS
INSERT INTO socios (id_socio, dni, nombre, apellido, edad, correo, telefono, estado) VALUES
(1001, '42100101', 'Camila', 'Torres', 27, 'camila.torres1001@test.com', '2664001001', 1),
(1002, '42100102', 'Mateo', 'Sosa', 32, 'mateo.sosa1002@test.com', '2664001002', 1),
(1003, '42100103', 'Valentina', 'Díaz', 24, 'valentina.diaz1003@test.com', '2664001003', 1),
(1004, '42100104', 'Tomás', 'Castro', 38, 'tomas.castro1004@test.com', '2664001004', 1),
(1005, '42100105', 'Julieta', 'Moreno', 29, 'julieta.moreno1005@test.com', '2664001005', 1),
(1006, '42100106', 'Bruno', 'Herrera', 41, 'bruno.herrera1006@test.com', '2664001006', 1),
(1007, '42100107', 'Milagros', 'Vega', 22, 'milagros.vega1007@test.com', '2664001007', 0),
(1008, '42100108', 'Facundo', 'Acosta', 35, 'facundo.acosta1008@test.com', '2664001008', 1);

-- CLASES
INSERT INTO clases (id_clase, nombre, id_entrenador, horario, capacidad, estado) VALUES
(1001, 'Musculación', 1001, '08:00:00', 15, 1),
(1002, 'Crossfit', 1002, '09:00:00', 12, 1),
(1003, 'Pilates', 1003, '10:00:00', 10, 1),
(1004, 'Spinning', 1004, '18:00:00', 16, 1),
(1005, 'Yoga', 1005, '19:00:00', 12, 1),
(1006, 'Funcional', 1001, '20:00:00', 14, 1),
(1007, 'Zumba (inactiva)', 1003, '17:00:00', 20, 0);

-- PASES
-- 1001: vigente con saldo; 1002: vigente con poco saldo; 1003: vencido;
-- 1004: futuro; 1005: inactivo; 1006: vigente sin saldo; 1008: vigente.
INSERT INTO pases (id_pase, id_socio, cantidadEntradas, fecha_inicio, fecha_fin, costo, estado) VALUES
(1001, 1001, 8, DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 25 DAY), 12000.00, 1),
(1002, 1002, 2, DATE_SUB(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 12000.00, 1),
(1003, 1003, 5, DATE_SUB(CURDATE(), INTERVAL 45 DAY), DATE_SUB(CURDATE(), INTERVAL 15 DAY), 12000.00, 1),
(1004, 1004, 12, DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 35 DAY), 18000.00, 1),
(1005, 1005, 8, DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 27 DAY), 12000.00, 0),
(1006, 1006, 0, DATE_SUB(CURDATE(), INTERVAL 8 DAY), DATE_ADD(CURDATE(), INTERVAL 22 DAY), 12000.00, 1),
(1008, 1008, 20, DATE_SUB(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 29 DAY), 25000.00, 1);

-- ASISTENCIAS DE EJEMPLO
INSERT INTO asistencias (id_asistencia, id_socio, id_clase, fecha_asistencia) VALUES
(1001, 1001, 1001, DATE_SUB(CURDATE(), INTERVAL 2 DAY)),
(1002, 1001, 1002, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
(1003, 1002, 1004, DATE_SUB(CURDATE(), INTERVAL 3 DAY)),
(1004, 1008, 1003, CURDATE());

COMMIT;

-- Para comprobar los datos cargados:
SELECT * FROM socios WHERE id_socio >= 1001;
SELECT * FROM clases WHERE id_clase >= 1001;
SELECT * FROM pases WHERE id_pase >= 1001;
SELECT * FROM asistencias WHERE id_asistencia >= 1001;
