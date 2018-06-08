-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: fitnet
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.13-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cobro`
--

DROP TABLE IF EXISTS `cobro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cobro` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL COMMENT 'fecha en la que se registra el cobro',
  `importeTotal` int(11) NOT NULL COMMENT 'suma de los precios de las actividades por cantidad de cada una menos el descuento',
  `entrega` int(11) NOT NULL COMMENT 'cantidad de dinero que entrega el cliente',
  `debe` int(11) NOT NULL COMMENT 'importe total menos la cantidad de dinero que entrega el cliente',
  `descuento` int(11) NOT NULL COMMENT 'descuento sobre la suma de los precios de las actividades por la canitadad de cada una',
  `motivoDescuento` varchar(45) NOT NULL COMMENT 'motivo de descuento',
  `fdesde` date DEFAULT NULL,
  `fhasta` date DEFAULT NULL,
  `id_bitacora` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `id_actividad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cobro_bitacora_idx` (`id_bitacora`),
  KEY `cobro_cliente_idx` (`id_cliente`),
  KEY `cobro_actividad_idx` (`id_actividad`),
  CONSTRAINT `cobro_actividad` FOREIGN KEY (`id_actividad`) REFERENCES `actividad` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cobro_bitacora` FOREIGN KEY (`id_bitacora`) REFERENCES `bitacora` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cobro_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cobro`
--

LOCK TABLES `cobro` WRITE;
/*!40000 ALTER TABLE `cobro` DISABLE KEYS */;
/*!40000 ALTER TABLE `cobro` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-28 13:11:30
