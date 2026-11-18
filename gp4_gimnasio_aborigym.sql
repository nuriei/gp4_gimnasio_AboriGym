-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-11-2026 a las 04:07:07
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gp4_gimnasio_aborigym`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencias`
--

CREATE TABLE `asistencias` (
  `id_asistencia` int(11) NOT NULL,
  `id_socio` int(11) NOT NULL,
  `id_clase` int(11) NOT NULL,
  `fecha_asistencia` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `asistencias`
--

INSERT INTO `asistencias` (`id_asistencia`, `id_socio`, `id_clase`, `fecha_asistencia`) VALUES
(1001, 1001, 1001, '2026-11-15'),
(1002, 1001, 1002, '2026-11-16'),
(1003, 1002, 1004, '2026-11-14'),
(1004, 1008, 1003, '2026-11-17'),
(1005, 1001, 1001, '2026-11-17'),
(1006, 1003, 1001, '2026-11-17'),
(1007, 1008, 1001, '2026-11-18'),
(1008, 1001, 1001, '2026-11-18'),
(1009, 1001, 1001, '2026-11-18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clases`
--

CREATE TABLE `clases` (
  `id_clase` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `id_entrenador` int(11) NOT NULL,
  `horario` time NOT NULL,
  `capacidad` int(11) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `clases`
--

INSERT INTO `clases` (`id_clase`, `nombre`, `id_entrenador`, `horario`, `capacidad`, `estado`) VALUES
(1001, 'Musculación', 1001, '08:00:00', 15, 1),
(1002, 'Crossfit', 1002, '09:00:00', 12, 1),
(1003, 'Pilates', 1003, '10:00:00', 10, 1),
(1004, 'Spinning', 1004, '18:00:00', 16, 1),
(1005, 'Yoga', 1005, '19:00:00', 12, 1),
(1006, 'Funcional', 1001, '20:00:00', 14, 1),
(1007, 'Zumba (inactiva)', 1003, '17:00:00', 20, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrenadores`
--

CREATE TABLE `entrenadores` (
  `id_entrenador` int(11) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `especialidad` varchar(100) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `entrenadores`
--

INSERT INTO `entrenadores` (`id_entrenador`, `dni`, `nombre`, `apellido`, `especialidad`, `estado`) VALUES
(2, '31265077', 'Rosario', 'Ibañez', 'YOGA', 1),
(4, '33816309', 'Nuri', 'ibañez', 'CROSSFIT', 0),
(5, '47846957', 'Juan', 'Figueroa', 'MUSCULACION', 1),
(6, '24587965', 'Sandra', 'Carreño', 'PILATES', 1),
(7, '45879684', 'Marcelo', 'Quiroga', 'FUNCIONAL', 0),
(1001, '40100101', 'Lucía', 'Fernández', 'MUSCULACION', 1),
(1002, '40100102', 'Marcos', 'Pereyra', 'CROSSFIT', 1),
(1003, '40100103', 'Sofía', 'Gómez', 'PILATES', 1),
(1004, '40100104', 'Diego', 'Luna', 'SPINNING', 1),
(1005, '40100105', 'Carolina', 'Ríos', 'YOGA', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pases`
--

CREATE TABLE `pases` (
  `id_pase` int(11) NOT NULL,
  `id_socio` int(11) NOT NULL,
  `cantidadEntradas` int(11) NOT NULL,
  `cantidad_inicial` int(11) NOT NULL DEFAULT 0,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `costo` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `pases`
--

INSERT INTO `pases` (`id_pase`, `id_socio`, `cantidadEntradas`, `cantidad_inicial`, `fecha_inicio`, `fecha_fin`, `costo`, `estado`) VALUES
(1001, 1001, 8, 0, '2026-11-12', '2026-12-12', 12000.00, 1),
(1002, 1002, 2, 0, '2026-11-07', '2026-12-07', 12000.00, 1),
(1003, 1003, 5, 0, '2026-10-03', '2026-11-02', 12000.00, 1),
(1004, 1004, 12, 0, '2026-11-22', '2026-12-22', 18000.00, 1),
(1005, 1005, 8, 0, '2026-11-14', '2026-12-14', 12000.00, 0),
(1006, 1006, 0, 0, '2026-11-09', '2026-12-09', 12000.00, 1),
(1008, 1008, 20, 0, '2026-11-16', '2026-12-16', 25000.00, 1),
(1009, 1001, 12, 12, '2026-11-17', '2026-12-17', 1800.00, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `socios`
--

CREATE TABLE `socios` (
  `id_socio` int(11) NOT NULL,
  `dni` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `edad` int(11) NOT NULL,
  `correo` varchar(50) NOT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `socios`
--

INSERT INTO `socios` (`id_socio`, `dni`, `nombre`, `apellido`, `edad`, `correo`, `telefono`, `estado`) VALUES
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
(1008, '42100108', 'Facundo', 'Acosta', 35, 'facundo.acosta1008@test.com', '2664001008', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asistencias`
--
ALTER TABLE `asistencias`
  ADD PRIMARY KEY (`id_asistencia`),
  ADD KEY `id_socio` (`id_socio`),
  ADD KEY `id_clase` (`id_clase`);

--
-- Indices de la tabla `clases`
--
ALTER TABLE `clases`
  ADD PRIMARY KEY (`id_clase`),
  ADD KEY `id_entrenador` (`id_entrenador`);

--
-- Indices de la tabla `entrenadores`
--
ALTER TABLE `entrenadores`
  ADD PRIMARY KEY (`id_entrenador`),
  ADD UNIQUE KEY `dni` (`dni`);

--
-- Indices de la tabla `pases`
--
ALTER TABLE `pases`
  ADD PRIMARY KEY (`id_pase`),
  ADD KEY `id_socio` (`id_socio`);

--
-- Indices de la tabla `socios`
--
ALTER TABLE `socios`
  ADD PRIMARY KEY (`id_socio`),
  ADD UNIQUE KEY `dni` (`dni`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asistencias`
--
ALTER TABLE `asistencias`
  MODIFY `id_asistencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1010;

--
-- AUTO_INCREMENT de la tabla `clases`
--
ALTER TABLE `clases`
  MODIFY `id_clase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1008;

--
-- AUTO_INCREMENT de la tabla `entrenadores`
--
ALTER TABLE `entrenadores`
  MODIFY `id_entrenador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1006;

--
-- AUTO_INCREMENT de la tabla `pases`
--
ALTER TABLE `pases`
  MODIFY `id_pase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1010;

--
-- AUTO_INCREMENT de la tabla `socios`
--
ALTER TABLE `socios`
  MODIFY `id_socio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1009;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asistencias`
--
ALTER TABLE `asistencias`
  ADD CONSTRAINT `fk_asistencia_clase` FOREIGN KEY (`id_clase`) REFERENCES `clases` (`id_clase`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_asistencia_socio` FOREIGN KEY (`id_socio`) REFERENCES `socios` (`id_socio`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `clases`
--
ALTER TABLE `clases`
  ADD CONSTRAINT `fk_clase_entrenador` FOREIGN KEY (`id_entrenador`) REFERENCES `entrenadores` (`id_entrenador`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `pases`
--
ALTER TABLE `pases`
  ADD CONSTRAINT `fk_membresia_socio` FOREIGN KEY (`id_socio`) REFERENCES `socios` (`id_socio`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
