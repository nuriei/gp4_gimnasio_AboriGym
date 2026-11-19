-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 19-11-2026 a las 07:08:04
-- Versión del servidor: 10.11.16-MariaDB
-- Versión de PHP: 8.3.36

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
(1106, 100, 1101, '2026-11-02'),
(1107, 100, 1102, '2026-08-10'),
(1108, 110, 1102, '2026-11-10'),
(1109, 102, 1104, '2026-11-09'),
(1110, 102, 1110, '2026-11-11'),
(1111, 102, 1107, '2026-11-13'),
(1112, 106, 1108, '2026-11-02'),
(1113, 106, 1106, '2026-11-05'),
(1114, 106, 1108, '2026-11-07'),
(1115, 106, 1106, '2026-11-09'),
(1116, 110, 1102, '2026-11-11'),
(1117, 110, 1110, '2026-11-13'),
(1118, 110, 1106, '2026-11-16'),
(1119, 110, 1107, '2026-11-18'),
(1120, 110, 1102, '2026-11-19'),
(1121, 113, 1101, '2026-11-09'),
(1122, 113, 1104, '2026-11-11'),
(1123, 113, 1101, '2026-11-13'),
(1124, 113, 1110, '2026-11-16'),
(1125, 113, 1102, '2026-11-18');

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
(1101, 'yoga', 100, '08:00:00', 10, 1),
(1102, 'funcional', 113, '10:00:00', 10, 1),
(1103, 'musculacion', 106, '10:00:00', 15, 0),
(1104, 'yoga', 100, '13:00:00', 7, 1),
(1105, 'yoga', 101, '17:00:00', 10, 0),
(1106, 'crossfit', 103, '19:00:00', 15, 1),
(1107, 'crossfit all levels', 104, '20:00:00', 10, 1),
(1108, 'musculacion pro', 106, '16:00:00', 5, 1),
(1109, 'musculacion', 107, '17:00:00', 10, 0),
(1110, 'funcional', 112, '16:00:00', 20, 1);

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
(100, '29411220', 'Martín', 'Gómez', 'YOGA', 1),
(101, '33890112', 'Lucía', 'Fernández', 'YOGA', 1),
(102, '27654321', 'Diego', 'Ruiz', 'YOGA', 0),
(103, '35123987', 'Camila', 'Sosa', 'CROSSFIT', 1),
(104, '38456129', 'Sebastián', 'Pereyra', 'CROSSFIT', 1),
(105, '26987410', 'Valeria', 'Díaz', 'CROSSFIT', 0),
(106, '31567890', 'Franco', 'Morales', 'MUSCULACION', 1),
(107, '36234567', 'Valentina', 'Acosta', 'MUSCULACION', 1),
(108, '40112233', 'Nicolás', 'Núñez', 'MUSCULACION', 0),
(109, '34789012', 'Florencia', 'Herrera', 'PILATES', 1),
(110, '32876543', 'Ezequiel', 'Quiroga', 'PILATES', 1),
(111, '37654981', 'Romina', 'Benítez', 'PILATES', 0),
(112, '28901456', 'Agustín', 'López', 'FUNCIONAL', 1),
(113, '39543210', 'Julieta', 'Martínez', 'FUNCIONAL', 1),
(114, '35890432', 'Matías', 'Rodríguez', 'FUNCIONAL', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pases`
--

CREATE TABLE `pases` (
  `id_Pase` int(11) NOT NULL,
  `id_Socio` int(11) NOT NULL,
  `cantidadEntradas` int(11) NOT NULL,
  `cantidad_inicial` int(11) NOT NULL,
  `fecha_Inicio` date NOT NULL,
  `fecha_Fin` date NOT NULL,
  `costo` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `pases`
--

INSERT INTO `pases` (`id_Pase`, `id_Socio`, `cantidadEntradas`, `cantidad_inicial`, `fecha_Inicio`, `fecha_Fin`, `costo`, `estado`) VALUES
(1101, 100, 6, 8, '2026-11-19', '2026-12-19', 1000.00, 1),
(1102, 102, 9, 12, '2026-11-09', '2026-12-09', 15000.00, 1),
(1103, 103, 8, 8, '2026-10-08', '2026-11-07', 10000.00, 0),
(1104, 104, 12, 12, '2026-07-03', '2026-08-02', 15000.00, 0),
(1105, 110, 14, 20, '2026-11-19', '2026-12-19', 20000.00, 1),
(1106, 108, 20, 20, '2026-11-03', '2026-12-03', 20000.00, 0),
(1107, 112, 20, 8, '2026-07-06', '2026-08-05', 10000.00, 1),
(1108, 113, 7, 12, '2026-11-16', '2026-12-16', 15000.00, 1),
(1109, 106, 4, 8, '2026-11-02', '2026-12-02', 10000.00, 1);

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
  `correo` varchar(100) NOT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;

--
-- Volcado de datos para la tabla `socios`
--

INSERT INTO `socios` (`id_socio`, `dni`, `nombre`, `apellido`, `edad`, `correo`, `telefono`, `estado`) VALUES
(100, '34892110', 'Juan', 'Perez', 31, 'juan.perez@gmail.com', '2664512340', 1),
(101, '40125678', 'Juan', 'Gómez', 26, 'jgomez99@hotmail.com', '2664893211', 0),
(102, '28456901', 'Juan', 'Perez', 43, 'juanperez.sl@yahoo.com.ar', '2665129874', 1),
(103, '38901234', 'María', 'Fernández', 28, 'maria.fernandez@gmail.com', '2664774120', 1),
(104, '42567890', 'Soledad', 'Rodríguez', 23, 'mrodriguez@outlook.com', '2664332198', 1),
(105, '19876543', 'Paola', 'Lopez', 56, 'mlopez.sanluis@gmail.com', '2664219087', 0),
(106, '45112233', 'Lucas', 'Martínez', 19, 'lucas.martinez@gmail.com', '2665401122', 1),
(107, '36789456', 'Claudio', 'Sosa', 30, 'lucassosa.entrenamiento@gmail.com', '2664908765', 1),
(108, '47890123', 'Sofía', 'Solis', 16, 'sofia.nunez@gmail.com', '2664654321', 1),
(109, '31234567', 'Sofía', 'Morales', 37, 'sofi.morales@live.com.ar', '2664112233', 0),
(110, '9456789', 'Carlos', 'Pereyra', 68, 'cpereyra58@gmail.com', '2664556677', 1),
(111, '8765432', 'Ana', 'Diaz', 71, 'carlos.diaz.sl@gmail.com', '2664889900', 0),
(112, '41987654', 'Valentina', 'Quiroga', 24, 'valen.quiroga@gmail.com', '2665789012', 1),
(113, '39654321', 'Gabriela', 'Benitez', 29, 'vbenitez@gmail.com', '2664345678', 1),
(114, '44321987', 'Martin', 'Acosta', 21, 'martin.acosta@gmail.com', '2664998877', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asistencias`
--
ALTER TABLE `asistencias`
  ADD PRIMARY KEY (`id_asistencia`),
  ADD KEY `idx_asistencias_socio` (`id_socio`),
  ADD KEY `idx_asistencias_clase` (`id_clase`);

--
-- Indices de la tabla `clases`
--
ALTER TABLE `clases`
  ADD PRIMARY KEY (`id_clase`),
  ADD KEY `idx_clases_entrenador` (`id_entrenador`);

--
-- Indices de la tabla `entrenadores`
--
ALTER TABLE `entrenadores`
  ADD PRIMARY KEY (`id_entrenador`),
  ADD UNIQUE KEY `uk_entrenadores_dni` (`dni`);

--
-- Indices de la tabla `pases`
--
ALTER TABLE `pases`
  ADD PRIMARY KEY (`id_Pase`),
  ADD KEY `idx_pases_socio` (`id_Socio`);

--
-- Indices de la tabla `socios`
--
ALTER TABLE `socios`
  ADD PRIMARY KEY (`id_socio`),
  ADD UNIQUE KEY `uk_socios_dni` (`dni`),
  ADD UNIQUE KEY `uk_socios_correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asistencias`
--
ALTER TABLE `asistencias`
  MODIFY `id_asistencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1126;

--
-- AUTO_INCREMENT de la tabla `clases`
--
ALTER TABLE `clases`
  MODIFY `id_clase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1111;

--
-- AUTO_INCREMENT de la tabla `entrenadores`
--
ALTER TABLE `entrenadores`
  MODIFY `id_entrenador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

--
-- AUTO_INCREMENT de la tabla `pases`
--
ALTER TABLE `pases`
  MODIFY `id_Pase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1110;

--
-- AUTO_INCREMENT de la tabla `socios`
--
ALTER TABLE `socios`
  MODIFY `id_socio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=115;

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
  ADD CONSTRAINT `fk_membresia_socio` FOREIGN KEY (`id_Socio`) REFERENCES `socios` (`id_socio`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
