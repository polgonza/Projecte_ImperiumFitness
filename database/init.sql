/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: 10.147.17.250    Database: imperium_fitness
-- ------------------------------------------------------
-- Server version	11.8.6-MariaDB-ubu2404

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
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
(10,'Yoga Restauratiu','Classe de ioga relaxant','2026-04-30 15:00:00',15,1),
(11,'HIIT','HIIT - sessió setmanal','2026-05-01 07:30:00',20,1),
(12,'Pilates','Pilates - sessió setmanal','2026-05-01 10:00:00',12,1),
(13,'Spinning','Spinning - sessió setmanal','2026-05-01 18:00:00',20,1),
(14,'CrossFit','CrossFit - sessió setmanal','2026-05-02 09:00:00',20,1),
(15,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-02 10:30:00',15,1),
(16,'Zumba','Zumba - sessió setmanal','2026-05-02 12:00:00',25,1),
(17,'Spinning','Spinning - sessió setmanal','2026-05-04 09:00:00',20,1),
(18,'Zumba','Zumba - sessió setmanal','2026-05-04 11:00:00',25,1),
(19,'Body Pump','Body Pump - sessió setmanal','2026-05-05 10:00:00',15,1),
(20,'CrossFit','CrossFit - sessió setmanal','2026-05-06 08:00:00',20,1),
(21,'Pilates','Pilates - sessió setmanal','2026-05-06 10:00:00',12,1),
(22,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-07 17:00:00',15,1),
(23,'HIIT','HIIT - sessió setmanal','2026-05-08 07:30:00',20,1),
(24,'Pilates','Pilates - sessió setmanal','2026-05-08 10:00:00',12,1),
(25,'Spinning','Spinning - sessió setmanal','2026-05-08 18:00:00',20,1),
(26,'CrossFit','CrossFit - sessió setmanal','2026-05-09 09:00:00',20,1),
(27,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-09 10:30:00',15,1),
(28,'Zumba','Zumba - sessió setmanal','2026-05-09 12:00:00',25,1),
(29,'Spinning','Spinning - sessió setmanal','2026-05-11 09:00:00',20,1),
(30,'Zumba','Zumba - sessió setmanal','2026-05-11 11:00:00',25,1),
(31,'Body Pump','Body Pump - sessió setmanal','2026-05-12 10:00:00',15,1),
(32,'CrossFit','CrossFit - sessió setmanal','2026-05-13 08:00:00',20,1),
(33,'Pilates','Pilates - sessió setmanal','2026-05-13 10:00:00',12,1),
(34,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-14 17:00:00',15,1),
(35,'HIIT','HIIT - sessió setmanal','2026-05-15 07:30:00',20,1),
(36,'Pilates','Pilates - sessió setmanal','2026-05-15 10:00:00',12,1),
(37,'Spinning','Spinning - sessió setmanal','2026-05-15 18:00:00',20,1),
(38,'CrossFit','CrossFit - sessió setmanal','2026-05-16 09:00:00',20,1),
(39,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-16 10:30:00',15,1),
(40,'Zumba','Zumba - sessió setmanal','2026-05-16 12:00:00',25,1),
(41,'Spinning','Spinning - sessió setmanal','2026-05-18 09:00:00',20,1),
(42,'Zumba','Zumba - sessió setmanal','2026-05-18 11:00:00',25,1),
(43,'Body Pump','Body Pump - sessió setmanal','2026-05-19 10:00:00',15,1),
(44,'CrossFit','CrossFit - sessió setmanal','2026-05-20 08:00:00',20,1),
(45,'Pilates','Pilates - sessió setmanal','2026-05-20 10:00:00',12,1),
(46,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-21 17:00:00',15,1),
(47,'HIIT','HIIT - sessió setmanal','2026-05-22 07:30:00',20,1),
(48,'Pilates','Pilates - sessió setmanal','2026-05-22 10:00:00',12,1),
(49,'Spinning','Spinning - sessió setmanal','2026-05-22 18:00:00',20,1),
(50,'CrossFit','CrossFit - sessió setmanal','2026-05-23 09:00:00',20,1),
(51,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-23 10:30:00',15,1),
(52,'Zumba','Zumba - sessió setmanal','2026-05-23 12:00:00',25,1),
(53,'Spinning','Spinning - sessió setmanal','2026-05-25 09:00:00',20,1),
(54,'Zumba','Zumba - sessió setmanal','2026-05-25 11:00:00',25,1),
(55,'Body Pump','Body Pump - sessió setmanal','2026-05-26 10:00:00',15,1),
(56,'CrossFit','CrossFit - sessió setmanal','2026-05-27 08:00:00',20,1),
(57,'Pilates','Pilates - sessió setmanal','2026-05-27 10:00:00',12,1),
(58,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-28 17:00:00',15,1),
(59,'HIIT','HIIT - sessió setmanal','2026-05-29 07:30:00',20,1),
(60,'Pilates','Pilates - sessió setmanal','2026-05-29 10:00:00',12,1),
(61,'Spinning','Spinning - sessió setmanal','2026-05-29 18:00:00',20,1),
(62,'CrossFit','CrossFit - sessió setmanal','2026-05-30 09:00:00',20,1),
(63,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-05-30 10:30:00',15,1),
(64,'Zumba','Zumba - sessió setmanal','2026-05-30 12:00:00',25,1),
(65,'Spinning','Spinning - sessió setmanal','2026-06-01 09:00:00',20,1),
(66,'Zumba','Zumba - sessió setmanal','2026-06-01 11:00:00',25,1),
(67,'Body Pump','Body Pump - sessió setmanal','2026-06-02 10:00:00',15,1),
(68,'CrossFit','CrossFit - sessió setmanal','2026-06-03 08:00:00',20,1),
(69,'Pilates','Pilates - sessió setmanal','2026-06-03 10:00:00',12,1),
(70,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-04 17:00:00',15,1),
(71,'HIIT','HIIT - sessió setmanal','2026-06-05 07:30:00',20,1),
(72,'Pilates','Pilates - sessió setmanal','2026-06-05 10:00:00',12,1),
(73,'Spinning','Spinning - sessió setmanal','2026-06-05 18:00:00',20,1),
(74,'CrossFit','CrossFit - sessió setmanal','2026-06-06 09:00:00',20,1),
(75,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-06 10:30:00',15,1),
(76,'Zumba','Zumba - sessió setmanal','2026-06-06 12:00:00',25,1),
(77,'Spinning','Spinning - sessió setmanal','2026-06-08 09:00:00',20,1),
(78,'Zumba','Zumba - sessió setmanal','2026-06-08 11:00:00',25,1),
(79,'Body Pump','Body Pump - sessió setmanal','2026-06-09 10:00:00',15,1),
(80,'CrossFit','CrossFit - sessió setmanal','2026-06-10 08:00:00',20,1),
(81,'Pilates','Pilates - sessió setmanal','2026-06-10 10:00:00',12,1),
(82,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-11 17:00:00',15,1),
(83,'HIIT','HIIT - sessió setmanal','2026-06-12 07:30:00',20,1),
(84,'Pilates','Pilates - sessió setmanal','2026-06-12 10:00:00',12,1),
(85,'Spinning','Spinning - sessió setmanal','2026-06-12 18:00:00',20,1),
(86,'CrossFit','CrossFit - sessió setmanal','2026-06-13 09:00:00',20,1),
(87,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-13 10:30:00',15,1),
(88,'Zumba','Zumba - sessió setmanal','2026-06-13 12:00:00',25,1),
(89,'Spinning','Spinning - sessió setmanal','2026-06-15 09:00:00',20,1),
(90,'Zumba','Zumba - sessió setmanal','2026-06-15 11:00:00',25,1),
(91,'Body Pump','Body Pump - sessió setmanal','2026-06-16 10:00:00',15,1),
(92,'CrossFit','CrossFit - sessió setmanal','2026-06-17 08:00:00',20,1),
(93,'Pilates','Pilates - sessió setmanal','2026-06-17 10:00:00',12,1),
(94,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-18 17:00:00',15,1),
(95,'HIIT','HIIT - sessió setmanal','2026-06-19 07:30:00',20,1),
(96,'Pilates','Pilates - sessió setmanal','2026-06-19 10:00:00',12,1),
(97,'Spinning','Spinning - sessió setmanal','2026-06-19 18:00:00',20,1),
(98,'CrossFit','CrossFit - sessió setmanal','2026-06-20 09:00:00',20,1),
(99,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-20 10:30:00',15,1),
(100,'Zumba','Zumba - sessió setmanal','2026-06-20 12:00:00',25,1),
(101,'Spinning','Spinning - sessió setmanal','2026-06-22 09:00:00',20,1),
(102,'Zumba','Zumba - sessió setmanal','2026-06-22 11:00:00',25,1),
(103,'Body Pump','Body Pump - sessió setmanal','2026-06-23 10:00:00',15,1),
(104,'CrossFit','CrossFit - sessió setmanal','2026-06-24 08:00:00',20,1),
(105,'Pilates','Pilates - sessió setmanal','2026-06-24 10:00:00',12,1),
(106,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-25 17:00:00',15,1),
(107,'HIIT','HIIT - sessió setmanal','2026-06-26 07:30:00',20,1),
(108,'Pilates','Pilates - sessió setmanal','2026-06-26 10:00:00',12,1),
(109,'Spinning','Spinning - sessió setmanal','2026-06-26 18:00:00',20,1),
(110,'CrossFit','CrossFit - sessió setmanal','2026-06-27 09:00:00',20,1),
(111,'Yoga Restauratiu','Yoga Restauratiu - sessió setmanal','2026-06-27 10:30:00',15,1),
(112,'Zumba','Zumba - sessió setmanal','2026-06-27 12:00:00',25,1),
(113,'Spinning','Spinning - sessió setmanal','2026-06-29 09:00:00',20,1),
(114,'Zumba','Zumba - sessió setmanal','2026-06-29 11:00:00',25,1),
(115,'Body Pump','Body Pump - sessió setmanal','2026-06-30 10:00:00',15,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacte`
--

LOCK TABLES `contacte` WRITE;
/*!40000 ALTER TABLE `contacte` DISABLE KEYS */;
INSERT INTO `contacte` VALUES
(1,'Pere Martí','pere@gmail.com','Voldria informació sobre les tarifes','2026-04-16 23:21:13'),
(2,'dafa','rafa@gmail.com','jhvc','2026-05-05 14:44:36'),
(3,'pepe','pepe@correo.com','Aqui se lava ropa?','2026-05-05 14:54:51'),
(4,'pepe','pepe@correo.com','Aqui lavan ropa?','2026-05-05 14:57:32'),
(5,'dafa','rafa@gmail.com','pechuga','2026-05-06 16:24:15'),
(6,'dafa','rafa@gmail.com','fqgzbshsshshhs','2026-05-06 17:39:09'),
(7,'vsgsg','adad@asdadfj.com','asjfnaiufba','2026-05-06 17:57:19'),
(8,'dafa','rafa@gmail.com','hola buenas','2026-05-11 13:50:16');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gimnas`
--

LOCK TABLES `gimnas` WRITE;
/*!40000 ALTER TABLE `gimnas` DISABLE KEYS */;
INSERT INTO `gimnas` VALUES
(1,'Imperium Fitness','Carrer Major 123, Barcelona','932001234'),
(2,'Imperium Fitness Nord','Avinguda Diagonal 456, Barcelona','932005678'),
(3,'Imperium Fitness Nord','Avinguda Diagonal 456, Barcelona','932005678');
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
  `imatge_url` mediumtext DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producte`
--

LOCK TABLES `producte` WRITE;
/*!40000 ALTER TABLE `producte` DISABLE KEYS */;
INSERT INTO `producte` VALUES
(1,'Proteïna Whey 1kg','Proteïna de sèrum de llet sabor xocolata',29.99,'Suplement',42,NULL),
(2,'Samarreta Imperium','Samarreta tècnica oficial del gimnàs',19.99,'Roba',25,NULL),
(3,'Creatina 300g','Creatina monohidrat pura',19.99,'Suplement',32,NULL),
(4,'Barra de proteïna','Snack proteic sabor xocolata',2.99,'Suplement',92,NULL),
(5,'Guants gimnàs','Guants de training amb palmell reforçat',14.99,'Accesoris',25,NULL),
(6,'Bossa de gimnàs','Bossa esportiva 30L',29.99,'Accesoris',20,NULL),
(7,'Samarreta tècnica home','Samarreta dry-fit transpirable',24.99,'Roba',35,NULL),
(8,'Samarreta tècnica dona','Samarreta dry-fit transpirable',24.99,'Roba',35,NULL),
(9,'Malla esportiva','Malla compressió negra',34.99,'Roba',19,NULL),
(10,'Ampolla 750ml','Ampolla esportiva amb filtre',12.99,'Accesoris',50,NULL),
(11,'Proteïna Whey 2kg','Proteïna de sèrum sabor vainilla',49.99,'Suplement',30,NULL),
(12,'Omega-3','Àcids grassos essencials 90 càpsules',16.99,'Suplement',39,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
(9,4,3,'2026-04-28 13:26:05'),
(10,1,5,'2026-05-01 17:48:03'),
(11,1,6,'2026-05-01 17:48:05'),
(12,1,2,'2026-05-01 17:48:13'),
(13,1,10,'2026-05-01 18:04:57'),
(14,1,4,'2026-05-01 20:06:15'),
(15,1,8,'2026-05-01 20:06:52'),
(35,1,20,'2026-05-06 18:07:57'),
(36,1,21,'2026-05-06 18:17:55'),
(37,1,22,'2026-05-07 14:08:10'),
(38,5,28,'2026-05-07 14:40:18'),
(39,5,59,'2026-05-07 14:45:21'),
(40,1,1,'2026-05-07 15:15:21'),
(41,1,1,'2026-05-07 15:17:33'),
(42,5,23,'2026-05-07 17:26:17'),
(43,5,31,'2026-05-11 13:14:11'),
(44,5,41,'2026-05-11 13:15:22'),
(45,5,36,'2026-05-11 13:24:01'),
(46,5,40,'2026-05-11 13:25:49'),
(53,1,29,'2026-05-11 17:45:18'),
(63,2,34,'2026-05-14 17:07:56'),
(64,2,70,'2026-05-14 17:12:45'),
(65,2,67,'2026-05-14 17:12:54'),
(66,2,79,'2026-05-14 17:12:58'),
(67,2,91,'2026-05-14 17:13:03'),
(68,2,103,'2026-05-14 17:13:07'),
(69,2,115,'2026-05-14 17:13:11'),
(70,2,94,'2026-05-14 17:13:15'),
(71,2,82,'2026-05-14 17:13:19'),
(72,2,95,'2026-05-14 17:14:59'),
(73,2,96,'2026-05-14 17:15:04'),
(74,2,97,'2026-05-14 17:15:06'),
(75,2,107,'2026-05-14 17:15:10'),
(76,2,108,'2026-05-14 17:15:13'),
(77,2,109,'2026-05-14 17:15:16'),
(90,1,54,'2026-05-16 15:30:05');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarifa`
--

LOCK TABLES `tarifa` WRITE;
/*!40000 ALTER TABLE `tarifa` DISABLE KEYS */;
INSERT INTO `tarifa` VALUES
(2,'Quota Flex',29.99,'Quota mensual amb accés lliure',1),
(3,'Quota Prime',49.99,'Quota mensual premium amb tots els serveis',1);
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
  `tarifa_id` bigint(20) DEFAULT NULL,
  `tarifa_data_inici` timestamp NULL DEFAULT NULL,
  `tarifa_data_fi` timestamp NULL DEFAULT NULL,
  `tarifa_cancellada` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_usuari_tarifa` (`tarifa_id`),
  CONSTRAINT `fk_usuari_tarifa` FOREIGN KEY (`tarifa_id`) REFERENCES `tarifa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuari`
--

LOCK TABLES `usuari` WRITE;
/*!40000 ALTER TABLE `usuari` DISABLE KEYS */;
INSERT INTO `usuari` VALUES
(1,'Admin','admin@imperium.com','$2a$10$ru5ykaDgdlEaK26H/xAknuW3V/92RRszOFkof0aL8GcdInQk/uNa6','2026-04-16 18:24:35','ADMIN',2,'2026-05-16 20:38:37','2026-06-16 20:38:37',0),
(2,'Joan Garcia','joan@gmail.com','$2a$10$CpPuIpBsxRDkhyyKyAfcg.VMsNqH4yPBUTWYoGBUOGWyHvE8vklpW','2026-04-16 18:24:35','USER',2,'2026-05-14 17:07:38','2026-06-14 17:07:38',0),
(3,'Maria López','maria@gmail.com','$2a$10$ommhLP6snGAotlvt5L3Li.1vfKo5MQUhcPe/8Xv5BO3u8edSTDsvm','2026-04-20 13:10:06','USER',NULL,NULL,NULL,0),
(4,'paco paquito','paco@gmail.com','$2a$10$aA5R/KsWBkwzrNNd/B5IVOt7ItQD7cjgBsby7l2byLj.8YXTqbPcq','2026-04-28 13:22:49','USER',NULL,NULL,NULL,0),
(5,'Joan Garcio','joan7@test.com','$2a$10$ls4jNPxpeaBmagwUmT6YBOwXD9lwgVepnMAwLAxQjFEMVsL56k0YS','2026-05-06 16:57:02','USER',NULL,NULL,NULL,0),
(6,'Les','les@test.com','$2a$10$Bzt226iGiefgDLf2o2F6COA6SSL/A3qoNbK92e/8GWu5X3bb5xTEe','2026-05-11 16:57:14','USER',NULL,NULL,NULL,0),
(7,'les77','les77@test.com','$2a$10$pFsk6t.ET1Gl5XrQFCMEauLWXTXPGnyX80XykCxFoMmUAfR/Knu1S','2026-05-11 17:11:01','USER',NULL,NULL,NULL,0),
(8,'pablo','pablo19@imperium.com','$2a$10$BVhjX667LBB9OpP.MPq.eu5IKqIgUdWQQySRrvW7kwCQQ8.Ie8ggW','2026-05-16 15:20:18','USER',NULL,NULL,NULL,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venda`
--

LOCK TABLES `venda` WRITE;
/*!40000 ALTER TABLE `venda` DISABLE KEYS */;
INSERT INTO `venda` VALUES
(1,2,1,1,'2026-05-07 14:23:18'),
(2,9,1,1,'2026-05-14 13:20:16'),
(3,1,1,1,'2026-05-16 09:13:14'),
(4,3,1,1,'2026-05-16 09:14:04'),
(5,4,1,1,'2026-05-16 09:14:04'),
(6,3,1,2,'2026-05-16 10:51:20'),
(7,4,1,2,'2026-05-16 10:51:20'),
(8,2,1,1,'2026-05-16 11:57:16'),
(9,3,1,1,'2026-05-16 11:58:45'),
(10,4,1,4,'2026-05-16 11:58:45'),
(11,3,1,1,'2026-05-16 15:30:24'),
(12,3,1,1,'2026-05-16 15:30:57'),
(13,4,1,1,'2026-05-16 15:31:23'),
(14,12,1,6,'2026-05-16 15:34:38'),
(15,1,1,5,'2026-05-16 16:41:33'),
(16,2,1,1,'2026-05-16 17:06:47'),
(17,1,1,2,'2026-05-16 17:07:24'),
(18,3,8,1,'2026-05-16 19:19:17'),
(19,2,8,2,'2026-05-16 19:20:19'),
(20,3,8,1,'2026-05-16 19:29:48');
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

-- Dump completed on 2026-05-17 18:27:29
