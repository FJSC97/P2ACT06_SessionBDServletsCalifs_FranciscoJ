-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-07-2026 a las 23:36:29
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
-- Base de datos: `sistema_escolar`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumnos`
--

CREATE TABLE `alumnos` (
  `idAlumno` int(11) NOT NULL,
  `matricula` varchar(20) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `paterno` varchar(100) NOT NULL,
  `materno` varchar(100) NOT NULL,
  `correo` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alumnos`
--

INSERT INTO `alumnos` (`idAlumno`, `matricula`, `nombre`, `paterno`, `materno`, `correo`) VALUES
(5, '57231900095_i', 'Deyanira', 'Pascualeño', 'Tlacotempa', '57231900095_i@utrng.edu.mx'),
(7, '57231900102_i', 'Torito Casarrubias', 'Jessica', 'Edith', '57231900102_i@utrng.edu.mx'),
(8, '57231900086_i', 'Paola Yareni', 'Bello', 'Calixto', '57231900086_i@utrng.edu.mx'),
(10, '57231900101_i', 'Francisco Javier', 'Silverio', 'Cuesta', '57231900101_i@utrng.edu.mx'),
(11, '57231900099_i', 'Alma Delia', 'Sanchez', 'Juarez', '57231900099_i@utrng.edu.mx'),
(12, '57231900081_i', 'Iris Gabriela', 'Torres', 'Días', '57231900081_i@utrng.edu.mx'),
(13, '57231900100_i', 'Alex Javier', 'Santos', 'Nava', '57231900100_i@utrng.edu.mx'),
(14, '57231900104_i', 'Axel Anders', 'Trinidad', 'Jimenez', '57231900104_i@utrng.edu.mx'),
(15, '57231900096_i', 'Axel Uriel', 'Ramos', 'Lopez', '57231900096_i@utrng.edu.mx'),
(16, '57231900069_i', 'Emmanuel', 'Flores', 'Esteban', '57231900069_i@utrng.edu.mx'),
(17, '57231900072_i', 'Isai', 'Zamudio', 'Gutierrez', '57231900072_i@utrng.edu.mx'),
(18, '57221900108_i', 'Marco Antonio', 'DeAquino', 'Vargas', '57221900108_i@utrng.edu.mx'),
(19, '57191900150_i', 'Junior Gerardo', 'Ramirez', 'Galindo', '57191900150_i@utrng.edu.mx'),
(20, '57231900067_i', 'Mario', 'Cuevas', 'García', '57231900067_i@utrng.edu.mx'),
(21, '57231900076_i', 'Rosalinda', 'Moreno', 'Hernandez', '57231900076_i@utrng.edu.mx'),
(22, '57231900070_i', 'Vicente Tadeo', 'Gabriel', 'Gonzalez', '57231900070_i@utrng.edu.mx'),
(23, '57231900075_i', 'Victor', 'Jaimes', 'Vazquez', '57231900075_i@utrng.edu.mx');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `calificaciones`
--

CREATE TABLE `calificaciones` (
  `idCalificacion` int(11) NOT NULL,
  `idAlumno` int(11) NOT NULL,
  `idMateria` int(11) NOT NULL,
  `parcial` int(11) NOT NULL DEFAULT 1,
  `calificacion` decimal(4,1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `calificaciones`
--

INSERT INTO `calificaciones` (`idCalificacion`, `idAlumno`, `idMateria`, `parcial`, `calificacion`) VALUES
(19, 5, 1, 1, 9.0),
(20, 5, 1, 2, 8.0),
(21, 5, 1, 3, 9.0),
(22, 5, 2, 1, 9.0),
(23, 5, 2, 2, 8.0),
(24, 5, 2, 3, 9.0),
(25, 7, 1, 1, 9.0),
(26, 7, 1, 2, 8.0),
(27, 7, 1, 3, 9.0),
(28, 7, 2, 1, 10.0),
(29, 7, 2, 2, 10.0),
(30, 7, 2, 3, 10.0),
(31, 8, 1, 1, 10.0),
(32, 8, 1, 2, 9.0),
(33, 8, 1, 3, 8.0),
(34, 8, 2, 1, 9.0),
(35, 8, 2, 2, 10.0),
(36, 8, 2, 3, 9.0),
(43, 10, 1, 1, 10.0),
(44, 10, 1, 2, 9.0),
(45, 10, 1, 3, 10.0),
(46, 10, 2, 1, 9.0),
(47, 10, 2, 2, 9.0),
(48, 10, 2, 3, 8.0),
(49, 11, 1, 1, 8.0),
(50, 11, 1, 2, 9.0),
(51, 11, 1, 3, 9.0),
(52, 11, 2, 1, 9.0),
(53, 11, 2, 2, 10.0),
(54, 11, 2, 3, 10.0),
(55, 12, 1, 1, 9.0),
(56, 12, 1, 2, 9.0),
(57, 12, 1, 3, 8.0),
(58, 12, 2, 1, 9.0),
(59, 12, 2, 2, 9.0),
(60, 12, 2, 3, 10.0),
(61, 20, 1, 1, NULL),
(62, 20, 1, 2, NULL),
(63, 20, 1, 3, NULL),
(64, 20, 2, 1, NULL),
(65, 20, 2, 2, NULL),
(66, 20, 2, 3, NULL),
(67, 18, 1, 1, NULL),
(68, 18, 1, 2, NULL),
(69, 18, 1, 3, NULL),
(70, 18, 2, 1, NULL),
(71, 18, 2, 2, NULL),
(72, 18, 2, 3, NULL),
(73, 16, 1, 1, NULL),
(74, 16, 1, 2, NULL),
(75, 16, 1, 3, NULL),
(76, 16, 2, 1, NULL),
(77, 16, 2, 2, NULL),
(78, 16, 2, 3, NULL),
(79, 22, 1, 1, NULL),
(80, 22, 1, 2, NULL),
(81, 22, 1, 3, NULL),
(82, 22, 2, 1, NULL),
(83, 22, 2, 2, NULL),
(84, 22, 2, 3, NULL),
(85, 23, 1, 1, NULL),
(86, 23, 1, 2, NULL),
(87, 23, 1, 3, NULL),
(88, 23, 2, 1, NULL),
(89, 23, 2, 2, NULL),
(90, 23, 2, 3, NULL),
(91, 21, 1, 1, NULL),
(92, 21, 1, 2, NULL),
(93, 21, 1, 3, NULL),
(94, 21, 2, 1, NULL),
(95, 21, 2, 2, NULL),
(96, 21, 2, 3, NULL),
(97, 19, 1, 1, NULL),
(98, 19, 1, 2, NULL),
(99, 19, 1, 3, NULL),
(100, 19, 2, 1, NULL),
(101, 19, 2, 2, NULL),
(102, 19, 2, 3, NULL),
(103, 15, 1, 1, NULL),
(104, 15, 1, 2, NULL),
(105, 15, 1, 3, NULL),
(106, 15, 2, 1, NULL),
(107, 15, 2, 2, NULL),
(108, 15, 2, 3, NULL),
(109, 13, 1, 1, NULL),
(110, 13, 1, 2, NULL),
(111, 13, 1, 3, NULL),
(112, 13, 2, 1, NULL),
(113, 13, 2, 2, NULL),
(114, 13, 2, 3, NULL),
(115, 14, 1, 1, NULL),
(116, 14, 1, 2, NULL),
(117, 14, 1, 3, NULL),
(118, 14, 2, 1, NULL),
(119, 14, 2, 2, NULL),
(120, 14, 2, 3, NULL),
(121, 17, 1, 1, NULL),
(122, 17, 1, 2, NULL),
(123, 17, 1, 3, NULL),
(124, 17, 2, 1, NULL),
(125, 17, 2, 2, NULL),
(126, 17, 2, 3, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materias`
--

CREATE TABLE `materias` (
  `idMateria` int(11) NOT NULL,
  `nombreMateria` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `materias`
--

INSERT INTO `materias` (`idMateria`, `nombreMateria`) VALUES
(1, 'BD'),
(2, 'Desarrollo web');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `idUsuario` int(11) NOT NULL,
  `nombreCompleto` varchar(150) NOT NULL,
  `matricula` varchar(20) DEFAULT NULL,
  `correo` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL DEFAULT 'Alumno',
  `validar` tinyint(1) NOT NULL DEFAULT 0,
  `status` varchar(20) NOT NULL DEFAULT 'Inactivo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`idUsuario`, `nombreCompleto`, `matricula`, `correo`, `password`, `rol`, `validar`, `status`) VALUES
(1, 'Francisco Javier Silverio Cuesta', 'ADMIN', 'admin@gmail.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Administrador', 1, 'Activo'),
(5, 'Deyanira Pascualeño Tlacotempa', '57231900095_i', '57231900095_i@utrng.edu.mx', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Alumno', 1, 'Activo'),
(7, 'Paola Yareni Bello Calixto', '57231900086_i', '57231900086_i@utrng.edu.mx', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Alumno', 1, 'Activo'),
(8, 'Torito Casarrubias Jessica Edith', '57231900102_i', '57231900102_i@utrng.edu.mx', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Alumno', 1, 'Activo'),
(9, 'Alma Delia Sanchez Juarez', '57231900099_i', '57231900099_i@utrng.edu.mx', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Alumno', 1, 'Activo'),
(10, 'Iris Gabriela Torres Días', '57231900081_i', '57231900081_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(13, 'Francisco Javier Silverio Cuesta', '57231900101_i', '57231900101_i@utrng.edu.mx', 'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', 'Alumno', 1, 'Activo'),
(14, 'Mario Cuevas García', '57231900067_i', '57231900067_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(15, 'Marco Antonio DeAquino Vargas', '57221900108_i', '57221900108_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(16, 'Emmanuel Flores Esteban', '57231900069_i', '57231900069_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(17, 'Vicente Tadeo Gabriel Gonzalez', '57231900070_i', '57231900070_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(18, 'Isai Zamudio Gutierrez', '57231900072_i', '57231900072_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(19, 'Victor Jaimes Vazquez', '57231900075_i', '57231900075_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(20, 'Rosalinda Moreno Hernandez', '57231900076_i', '57231900076_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(21, 'Junior Gerardo Ramirez Galindo', '57191900150_i', '57191900150_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(22, 'Axel Uriel Ramos Lopez', '57231900096_i', '57231900096_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(23, 'Alex Javier Santos Nava', '57231900100_i', '57231900100_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo'),
(24, 'Axel Anders Trinidad Jimenez ', '57231900104_i', '57231900104_i@utrng.edu.mx', '12345678', 'Alumno', 1, 'Activo');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  ADD PRIMARY KEY (`idAlumno`);

--
-- Indices de la tabla `calificaciones`
--
ALTER TABLE `calificaciones`
  ADD PRIMARY KEY (`idCalificacion`),
  ADD UNIQUE KEY `uq_alumno_materia_parcial` (`idAlumno`,`idMateria`,`parcial`),
  ADD KEY `fk_calif_materia` (`idMateria`);

--
-- Indices de la tabla `materias`
--
ALTER TABLE `materias`
  ADD PRIMARY KEY (`idMateria`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`idUsuario`),
  ADD UNIQUE KEY `correo` (`correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alumnos`
--
ALTER TABLE `alumnos`
  MODIFY `idAlumno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `calificaciones`
--
ALTER TABLE `calificaciones`
  MODIFY `idCalificacion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=127;

--
-- AUTO_INCREMENT de la tabla `materias`
--
ALTER TABLE `materias`
  MODIFY `idMateria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `calificaciones`
--
ALTER TABLE `calificaciones`
  ADD CONSTRAINT `fk_calif_alumno` FOREIGN KEY (`idAlumno`) REFERENCES `alumnos` (`idAlumno`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_calif_materia` FOREIGN KEY (`idMateria`) REFERENCES `materias` (`idMateria`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
