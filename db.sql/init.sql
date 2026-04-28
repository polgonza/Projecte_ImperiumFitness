/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: imperium_fitness
-- ------------------------------------------------------
-- Server version	12.2.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `classe`
--

DROP TABLE IF EXISTS `classe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `classe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  `descripcio` text DEFAULT NULL,
  `horari` timestamp NULL DEFAULT NULL,
  `capacitat` int(11) DEFAULT NULL,
  `gimnas_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_classe_gimnas` (`gimnas_id`),
  CONSTRAINT `fk_classe_gimnas` FOREIGN KEY (`gimnas_id`) REFERENCES `gimnas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classe`
--

LOCK TABLES `classe` WRITE;
/*!40000 ALTER TABLE `classe` DISABLE KEYS */;
INSERT INTO `classe` VALUES
(1,'Spinning','Classe de ciclisme indoor d\'alta intensitat','2026-04-20 07:00:00',20,1),
(2,'Zumba','Ball fitness amb ritmes llatins','2026-04-20 09:00:00',25,1),
(3,'Body Pump','Entrenament muscular amb barra','2026-04-21 08:00:00',15,1),
(4,'Pilates','Exercicis de core i flexibilitat','2026-04-22 08:00:00',12,1),
(5,'Spinning Matinal','Classe de ciclisme indoor','2026-04-25 07:00:00',20,1),
(6,'Zumba Divendres','Ball fitness amb ritmes llatins','2026-04-25 09:00:00',25,1),
(7,'Body Pump','Entrenament muscular amb barra','2026-04-26 08:00:00',15,1),
(8,'Pilates Core','Exercicis de core i flexibilitat','2026-04-28 08:00:00',12,1),
(9,'CrossFit','Entrenament funcional alta intensitat','2026-04-29 06:00:00',20,1),
(10,'Yoga Restauratiu','Classe de ioga relaxant','2026-04-30 15:00:00',15,1);
/*!40000 ALTER TABLE `classe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacte`
--

DROP TABLE IF EXISTS `contacte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacte` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `missatge` varchar(1000) NOT NULL,
  `data_enviament` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacte`
--

LOCK TABLES `contacte` WRITE;
/*!40000 ALTER TABLE `contacte` DISABLE KEYS */;
INSERT INTO `contacte` VALUES
(1,'Pere Martí','pere@gmail.com','Voldria informació sobre les tarifes','2026-04-16 23:21:13');
/*!40000 ALTER TABLE `contacte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadistica`
--

DROP TABLE IF EXISTS `estadistica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadistica` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tipus` varchar(100) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  `data` timestamp NULL DEFAULT current_timestamp(),
  `gimnas_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_estadistica_gimnas` (`gimnas_id`),
  CONSTRAINT `fk_estadistica_gimnas` FOREIGN KEY (`gimnas_id`) REFERENCES `gimnas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadistica`
--

LOCK TABLES `estadistica` WRITE;
/*!40000 ALTER TABLE `estadistica` DISABLE KEYS */;
/*!40000 ALTER TABLE `estadistica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gimnas`
--

DROP TABLE IF EXISTS `gimnas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `gimnas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  `adreca` varchar(255) DEFAULT NULL,
  `telefon` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gimnas`
--

LOCK TABLES `gimnas` WRITE;
/*!40000 ALTER TABLE `gimnas` DISABLE KEYS */;
INSERT INTO `gimnas` VALUES
(1,'Imperium Fitness','Carrer Major 123, Barcelona','932001234'),
(2,'Imperium Fitness Nord','Avinguda Diagonal 456, Barcelona','932005678');
/*!40000 ALTER TABLE `gimnas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instalacio`
--

DROP TABLE IF EXISTS `instalacio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `instalacio` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  `descripcio` text DEFAULT NULL,
  `ubicacio` varchar(255) DEFAULT NULL,
  `gimnas_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_instalacio_gimnas` (`gimnas_id`),
  CONSTRAINT `fk_instalacio_gimnas` FOREIGN KEY (`gimnas_id`) REFERENCES `gimnas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instalacio`
--

LOCK TABLES `instalacio` WRITE;
/*!40000 ALTER TABLE `instalacio` DISABLE KEYS */;
/*!40000 ALTER TABLE `instalacio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `noticia`
--

DROP TABLE IF EXISTS `noticia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `noticia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `titol` varchar(500) DEFAULT NULL,
  `contingut` text DEFAULT NULL,
  `data_publicacio` timestamp NULL DEFAULT current_timestamp(),
  `autor_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_noticia_autor` (`autor_id`),
  CONSTRAINT `fk_noticia_autor` FOREIGN KEY (`autor_id`) REFERENCES `usuari` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `noticia`
--

LOCK TABLES `noticia` WRITE;
/*!40000 ALTER TABLE `noticia` DISABLE KEYS */;
INSERT INTO `noticia` VALUES
(1,'Benvinguts a Imperium Fitness!','Estem contents d\'obrir les portes del nostre gimnàs. Vine a conèixer-nos!','2026-04-16 18:24:35',1);
/*!40000 ALTER TABLE `noticia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagament`
--

DROP TABLE IF EXISTS `pagament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagament` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuari_id` bigint(20) DEFAULT NULL,
  `import_total` decimal(10,2) DEFAULT NULL,
  `data_pagament` timestamp NULL DEFAULT current_timestamp(),
  `metode_pagament` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pagament_usuari` (`usuari_id`),
  CONSTRAINT `fk_pagament_usuari` FOREIGN KEY (`usuari_id`) REFERENCES `usuari` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagament`
--

LOCK TABLES `pagament` WRITE;
/*!40000 ALTER TABLE `pagament` DISABLE KEYS */;
/*!40000 ALTER TABLE `pagament` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producte`
--

DROP TABLE IF EXISTS `producte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `producte` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `descripcio` varchar(500) DEFAULT NULL,
  `preu` decimal(10,2) DEFAULT NULL,
  `categoria` varchar(255) DEFAULT NULL,
  `estoc` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producte`
--

LOCK TABLES `producte` WRITE;
/*!40000 ALTER TABLE `producte` DISABLE KEYS */;
INSERT INTO `producte` VALUES
(1,'Proteïna Whey 1kg','Proteïna de sèrum de llet sabor xocolata',29.99,'Suplement',50),
(2,'Samarreta Imperium','Samarreta tècnica oficial del gimnàs',19.99,'Roba',30);
/*!40000 ALTER TABLE `producte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reserva`
--

DROP TABLE IF EXISTS `reserva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuari_id` bigint(20) DEFAULT NULL,
  `classe_id` bigint(20) DEFAULT NULL,
  `data_reserva` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_reserva_usuari` (`usuari_id`),
  KEY `fk_reserva_classe` (`classe_id`),
  CONSTRAINT `fk_reserva_classe` FOREIGN KEY (`classe_id`) REFERENCES `classe` (`id`),
  CONSTRAINT `fk_reserva_usuari` FOREIGN KEY (`usuari_id`) REFERENCES `usuari` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
INSERT INTO `reserva` VALUES
(1,2,1,'2026-04-16 23:05:50'),
(2,3,1,'2026-04-20 17:54:53'),
(3,3,2,'2026-04-20 18:01:11'),
(4,2,3,'2026-04-21 16:07:24'),
(5,2,8,'2026-04-21 16:10:50'),
(6,1,3,'2026-04-21 16:52:37'),
(7,1,9,'2026-04-22 16:30:31'),
(8,1,1,'2026-04-27 17:01:10'),
(9,4,3,'2026-04-28 13:26:05');
/*!40000 ALTER TABLE `reserva` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarifa`
--

DROP TABLE IF EXISTS `tarifa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarifa` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) DEFAULT NULL,
  `preu` decimal(10,2) DEFAULT NULL,
  `descripcio` text DEFAULT NULL,
  `gimnas_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tarifa_gimnas` (`gimnas_id`),
  CONSTRAINT `fk_tarifa_gimnas` FOREIGN KEY (`gimnas_id`) REFERENCES `gimnas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarifa`
--

LOCK TABLES `tarifa` WRITE;
/*!40000 ALTER TABLE `tarifa` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarifa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuari`
--

DROP TABLE IF EXISTS `usuari`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuari` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contrasenya` varchar(255) NOT NULL,
  `data_registre` timestamp NULL DEFAULT current_timestamp(),
  `rol` varchar(50) DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuari`
--

LOCK TABLES `usuari` WRITE;
/*!40000 ALTER TABLE `usuari` DISABLE KEYS */;
INSERT INTO `usuari` VALUES
(1,'Admin','admin@imperium.com','$2a$10$ru5ykaDgdlEaK26H/xAknuW3V/92RRszOFkof0aL8GcdInQk/uNa6','2026-04-16 18:24:35','ADMIN'),
(2,'Joan Garcia','joan@gmail.com','$2a$10$CpPuIpBsxRDkhyyKyAfcg.VMsNqH4yPBUTWYoGBUOGWyHvE8vklpW','2026-04-16 18:24:35','USER'),
(3,'Maria López','maria@gmail.com','$2a$10$ommhLP6snGAotlvt5L3Li.1vfKo5MQUhcPe/8Xv5BO3u8edSTDsvm','2026-04-20 13:10:06','USER'),
(4,'paco paquito','paco@gmail.com','$2a$10$aA5R/KsWBkwzrNNd/B5IVOt7ItQD7cjgBsby7l2byLj.8YXTqbPcq','2026-04-28 13:22:49','USER');
/*!40000 ALTER TABLE `usuari` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venda`
--

DROP TABLE IF EXISTS `venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `venda` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producte_id` bigint(20) DEFAULT NULL,
  `usuari_id` bigint(20) DEFAULT NULL,
  `quantitat` int(11) DEFAULT NULL,
  `data_venda` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `fk_venda_producte` (`producte_id`),
  KEY `fk_venda_usuari` (`usuari_id`),
  CONSTRAINT `fk_venda_producte` FOREIGN KEY (`producte_id`) REFERENCES `producte` (`id`),
  CONSTRAINT `fk_venda_usuari` FOREIGN KEY (`usuari_id`) REFERENCES `usuari` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venda`
--

LOCK TABLES `venda` WRITE;
/*!40000 ALTER TABLE `venda` DISABLE KEYS */;
/*!40000 ALTER TABLE `venda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'imperium_fitness'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-04-28 16:20:18