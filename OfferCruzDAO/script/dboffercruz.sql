-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-12-2014 a las 15:20:46
-- Versión del servidor: 5.5.32
-- Versión de PHP: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `dboffercruz`
--
CREATE DATABASE IF NOT EXISTS `dboffercruz` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `dboffercruz`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE IF NOT EXISTS `categoria` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) NOT NULL,
  `Tipo` int(11) NOT NULL,
  `Estado` int(11) NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`Id`, `Nombre`, `Tipo`, `Estado`, `FechaCreacion`, `FechaModificacion`) VALUES
(1, 'Ernesto', 1, 1, '2014-12-02 10:10:24', '2014-12-02 10:10:24');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE IF NOT EXISTS `cliente` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdUsuario` int(11) NOT NULL,
  `IdImagen` int(11) NOT NULL,
  `Nombre` varchar(50) NOT NULL,
  `Apellido` varchar(50) NOT NULL,
  `Telefono` varchar(10) NOT NULL,
  `Estado` int(11) NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  `Bio` varchar(255) DEFAULT NULL,
  `Genero` varchar(2) DEFAULT NULL,
  `FechaNacimiento` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_jxtjaqynuuwam48mhkx5f5toj` (`IdUsuario`),
  KEY `FK_4om8pbw6oxbg74oa8brat2h4n` (`IdImagen`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contenido`
--

CREATE TABLE IF NOT EXISTS `contenido` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `TipoContenido` int(11) NOT NULL,
  `Titulo` varchar(40) NOT NULL,
  `Descripcion` varchar(244) NOT NULL,
  `Estado` int(11) NOT NULL,
  `FechaPublicacion` datetime NOT NULL,
  `FechaExpiracion` datetime NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallecontenido`
--

CREATE TABLE IF NOT EXISTS `detallecontenido` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOferta` int(11) NOT NULL,
  `IdContenido` int(11) NOT NULL,
  `FechaAsignacion` datetime NOT NULL,
  `PrecioUnitarioOferta` double DEFAULT NULL,
  `TipoOfertaDetalleContenido` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_msvwflrjfv1o9vpx8nlj6sw9j` (`IdOferta`),
  KEY `FK_7k2npeaxhr8xhb8xf4gjx2k9n` (`IdContenido`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallesolicitud`
--

CREATE TABLE IF NOT EXISTS `detallesolicitud` (
  `Id` int(11) NOT NULL,
  `IdSolicitud` int(11) NOT NULL,
  `IdOferta` int(11) NOT NULL,
  `IdDetalleContenido` int(11) DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  `PrecioUnitario` double DEFAULT NULL,
  `MontoDetalle` double DEFAULT NULL,
  `TipoOfertaDetalleSolicitud` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ki8la1wtjyskh987ccywxaqmy` (`IdSolicitud`),
  KEY `FK_olycovhrhvjmbyqxdy9yfb793` (`IdOferta`),
  KEY `FK_3jmpqaewgsvbq2bcnkqwagdkd` (`IdDetalleContenido`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresa`
--

CREATE TABLE IF NOT EXISTS `empresa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdLogotipo` int(11) DEFAULT NULL,
  `IdUsuario` int(11) NOT NULL,
  `Estado` int(11) NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  `RazonSocial` varchar(50) NOT NULL,
  `Slogan` varchar(100) DEFAULT NULL,
  `Mision` varchar(100) DEFAULT NULL,
  `Vision` varchar(100) DEFAULT NULL,
  `Telefono` varchar(10) DEFAULT NULL,
  `Direccion` varchar(100) DEFAULT NULL,
  `TipoSociedad` varchar(30) DEFAULT NULL,
  `FechaApertura` datetime DEFAULT NULL,
  `Nit` varchar(50) DEFAULT NULL,
  `TipoOferta` int(11) NOT NULL,
  `Fax` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_1m3b40ifa560q5mfy0pt74qke` (`IdLogotipo`),
  KEY `FK_aw69s8f8g51ij89hnd2q2x0um` (`IdUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empresacategoria`
--

CREATE TABLE IF NOT EXISTS `empresacategoria` (
  `idEmpresa` int(11) NOT NULL,
  `idCategoria` int(11) NOT NULL,
  PRIMARY KEY (`idCategoria`,`idEmpresa`),
  KEY `FK_4uwkgn8lrhhhpre938bn66a4x` (`idCategoria`),
  KEY `FK_31oxytnkrwh77p5kuowr87iv1` (`idEmpresa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `imagecontenido`
--

CREATE TABLE IF NOT EXISTS `imagecontenido` (
  `idImagen` int(11) NOT NULL,
  `idContenido` int(11) NOT NULL,
  PRIMARY KEY (`idContenido`,`idImagen`),
  KEY `FK_ha03ryrlxtt43ewrnpsp0cjlb` (`idContenido`),
  KEY `FK_si6j24xa6t0dq1hvjfessfbu9` (`idImagen`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `imagen`
--

CREATE TABLE IF NOT EXISTS `imagen` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(250) NOT NULL,
  `Ancho` int(11) DEFAULT NULL,
  `Alto` int(11) DEFAULT NULL,
  `FechaCreacion` datetime NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  `Estado` int(11) NOT NULL,
  `ImagenFisica` longtext NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `oferta`
--

CREATE TABLE IF NOT EXISTS `oferta` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdImagen` int(11) DEFAULT NULL,
  `IdCategoria` int(11) NOT NULL,
  `IdEmpresa` int(11) NOT NULL,
  `Descripcion` varchar(255) NOT NULL,
  `Nombre` varchar(50) NOT NULL,
  `Estado` int(11) NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  `TipoOferta` int(11) NOT NULL,
  `PrecioUnitario` double DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_g6vqg89quph9bvv8k9evv25t` (`IdImagen`),
  KEY `FK_bbhqf9g4hyc17veff1r5jdmec` (`IdCategoria`),
  KEY `FK_kx8fgbkk5w60j3jg096ll63t8` (`IdEmpresa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `perfil`
--

CREATE TABLE IF NOT EXISTS `perfil` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) NOT NULL,
  `Descripcion` varchar(200) NOT NULL,
  `Estado` int(11) NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `perfil`
--

INSERT INTO `perfil` (`Id`, `Nombre`, `Descripcion`, `Estado`, `FechaModificacion`, `FechaCreacion`) VALUES
(1, 'Administrador usuarios', 'Administrador Global del sistema Offercruz', 1, '2014-12-02 00:00:00', '2014-12-02 00:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `perfilpermiso`
--

CREATE TABLE IF NOT EXISTS `perfilpermiso` (
  `idPerfil` int(11) NOT NULL,
  `idPermiso` int(11) NOT NULL,
  PRIMARY KEY (`idPermiso`,`idPerfil`),
  KEY `FK_picnu5ku7100vsx4hij1k4f15` (`idPermiso`),
  KEY `FK_p5refcrd2wciobv3vdf47ob31` (`idPerfil`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `perfilpermiso`
--

INSERT INTO `perfilpermiso` (`idPerfil`, `idPermiso`) VALUES
(1, 1000),
(1, 2000),
(1, 3000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permiso`
--

CREATE TABLE IF NOT EXISTS `permiso` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `PermisoTexto` varchar(50) DEFAULT NULL,
  `PermisoPadreId` int(11) DEFAULT NULL,
  `Estado` int(11) NOT NULL,
  `PermisoPagina` varchar(50) DEFAULT NULL,
  `Comando` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3001 ;

--
-- Volcado de datos para la tabla `permiso`
--

INSERT INTO `permiso` (`Id`, `PermisoTexto`, `PermisoPadreId`, `Estado`, `PermisoPagina`, `Comando`) VALUES
(1000, 'Usuario', NULL, 1, 'usuario.jsf', 'usuario'),
(2000, 'Perfil', NULL, 1, 'perfil.jsf', 'perfil'),
(3000, 'Categoria', NULL, 1, 'categoria.jsf', 'categoria');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitud`
--

CREATE TABLE IF NOT EXISTS `solicitud` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdCliente` int(11) NOT NULL,
  `IdEmpresa` int(11) NOT NULL,
  `IdContenido` int(11) DEFAULT NULL,
  `Estado` int(11) NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  `TelefonoClienteTransaccion` varchar(10) NOT NULL,
  `TotalEstimadoSolicitud` double DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_phfr0rguoxugc74932r4l9kwl` (`IdCliente`),
  KEY `FK_bqmc217ifq0l5tpbo67ip54tk` (`IdEmpresa`),
  KEY `FK_totxdqx2cfin5b3sw7okjdj06` (`IdContenido`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subscripcion`
--

CREATE TABLE IF NOT EXISTS `subscripcion` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdCliente` int(11) NOT NULL,
  `IdEmpresa` int(11) NOT NULL,
  `FechaSubscripcion` datetime NOT NULL,
  `Estado` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_9fsx5u9a6ltalb4gnkvn1db9f` (`IdCliente`),
  KEY `FK_4n4twtwcpa14jv1ulxux1nsbd` (`IdEmpresa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdPerfil` int(11) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Login` varchar(20) NOT NULL,
  `FechaModificacion` datetime NOT NULL,
  `Estado` int(11) NOT NULL,
  `FechaCreacion` datetime NOT NULL,
  `Tipo` int(11) NOT NULL,
  `CorreoElectronico` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UK_iue3nl9h7cy8jaufdl54dc712` (`Login`),
  KEY `FK_ag41804212pd80dpbk041n2gl` (`IdPerfil`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`Id`, `IdPerfil`, `Password`, `Login`, `FechaModificacion`, `Estado`, `FechaCreacion`, `Tipo`, `CorreoElectronico`) VALUES
(1, 1, '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin', '2014-12-02 00:00:00', 1, '2014-12-02 00:00:00', 0, 'offercruz@gmail.com');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `FK_4om8pbw6oxbg74oa8brat2h4n` FOREIGN KEY (`IdImagen`) REFERENCES `imagen` (`Id`),
  ADD CONSTRAINT `FK_jxtjaqynuuwam48mhkx5f5toj` FOREIGN KEY (`IdUsuario`) REFERENCES `usuario` (`Id`);

--
-- Filtros para la tabla `detallecontenido`
--
ALTER TABLE `detallecontenido`
  ADD CONSTRAINT `FK_7k2npeaxhr8xhb8xf4gjx2k9n` FOREIGN KEY (`IdContenido`) REFERENCES `contenido` (`Id`),
  ADD CONSTRAINT `FK_msvwflrjfv1o9vpx8nlj6sw9j` FOREIGN KEY (`IdOferta`) REFERENCES `oferta` (`Id`);

--
-- Filtros para la tabla `detallesolicitud`
--
ALTER TABLE `detallesolicitud`
  ADD CONSTRAINT `FK_3jmpqaewgsvbq2bcnkqwagdkd` FOREIGN KEY (`IdDetalleContenido`) REFERENCES `detallecontenido` (`Id`),
  ADD CONSTRAINT `FK_ki8la1wtjyskh987ccywxaqmy` FOREIGN KEY (`IdSolicitud`) REFERENCES `solicitud` (`Id`),
  ADD CONSTRAINT `FK_olycovhrhvjmbyqxdy9yfb793` FOREIGN KEY (`IdOferta`) REFERENCES `oferta` (`Id`);

--
-- Filtros para la tabla `empresa`
--
ALTER TABLE `empresa`
  ADD CONSTRAINT `FK_aw69s8f8g51ij89hnd2q2x0um` FOREIGN KEY (`IdUsuario`) REFERENCES `usuario` (`Id`),
  ADD CONSTRAINT `FK_1m3b40ifa560q5mfy0pt74qke` FOREIGN KEY (`IdLogotipo`) REFERENCES `imagen` (`Id`);

--
-- Filtros para la tabla `empresacategoria`
--
ALTER TABLE `empresacategoria`
  ADD CONSTRAINT `FK_31oxytnkrwh77p5kuowr87iv1` FOREIGN KEY (`idEmpresa`) REFERENCES `empresa` (`Id`),
  ADD CONSTRAINT `FK_4uwkgn8lrhhhpre938bn66a4x` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`Id`);

--
-- Filtros para la tabla `imagecontenido`
--
ALTER TABLE `imagecontenido`
  ADD CONSTRAINT `FK_si6j24xa6t0dq1hvjfessfbu9` FOREIGN KEY (`idImagen`) REFERENCES `imagen` (`Id`),
  ADD CONSTRAINT `FK_ha03ryrlxtt43ewrnpsp0cjlb` FOREIGN KEY (`idContenido`) REFERENCES `contenido` (`Id`);

--
-- Filtros para la tabla `oferta`
--
ALTER TABLE `oferta`
  ADD CONSTRAINT `FK_kx8fgbkk5w60j3jg096ll63t8` FOREIGN KEY (`IdEmpresa`) REFERENCES `empresa` (`Id`),
  ADD CONSTRAINT `FK_bbhqf9g4hyc17veff1r5jdmec` FOREIGN KEY (`IdCategoria`) REFERENCES `categoria` (`Id`),
  ADD CONSTRAINT `FK_g6vqg89quph9bvv8k9evv25t` FOREIGN KEY (`IdImagen`) REFERENCES `imagen` (`Id`);

--
-- Filtros para la tabla `perfilpermiso`
--
ALTER TABLE `perfilpermiso`
  ADD CONSTRAINT `FK_p5refcrd2wciobv3vdf47ob31` FOREIGN KEY (`idPerfil`) REFERENCES `perfil` (`Id`),
  ADD CONSTRAINT `FK_picnu5ku7100vsx4hij1k4f15` FOREIGN KEY (`idPermiso`) REFERENCES `permiso` (`Id`);

--
-- Filtros para la tabla `solicitud`
--
ALTER TABLE `solicitud`
  ADD CONSTRAINT `FK_totxdqx2cfin5b3sw7okjdj06` FOREIGN KEY (`IdContenido`) REFERENCES `contenido` (`Id`),
  ADD CONSTRAINT `FK_bqmc217ifq0l5tpbo67ip54tk` FOREIGN KEY (`IdEmpresa`) REFERENCES `empresa` (`Id`),
  ADD CONSTRAINT `FK_phfr0rguoxugc74932r4l9kwl` FOREIGN KEY (`IdCliente`) REFERENCES `cliente` (`Id`);

--
-- Filtros para la tabla `subscripcion`
--
ALTER TABLE `subscripcion`
  ADD CONSTRAINT `FK_4n4twtwcpa14jv1ulxux1nsbd` FOREIGN KEY (`IdEmpresa`) REFERENCES `empresa` (`Id`),
  ADD CONSTRAINT `FK_9fsx5u9a6ltalb4gnkvn1db9f` FOREIGN KEY (`IdCliente`) REFERENCES `cliente` (`Id`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `FK_ag41804212pd80dpbk041n2gl` FOREIGN KEY (`IdPerfil`) REFERENCES `perfil` (`Id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
