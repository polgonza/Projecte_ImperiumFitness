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
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
(115,'Body Pump','Body Pump - sessió setmanal','2026-06-30 10:00:00',15,1),
(116,'Pajichuela','Lo mas rico del mundo en verda','2026-05-18 20:20:00',1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
(12,'Omega-3','Àcids grassos essencials 90 càpsules',16.99,'Suplement',38,NULL),
(13,'Pepe Viyuela','Un mostro un fiera un fenomeno',999.00,'Roba',0,'data:image/jpeg;base64,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');
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
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuari`
--

LOCK TABLES `usuari` WRITE;
/*!40000 ALTER TABLE `usuari` DISABLE KEYS */;
INSERT INTO `usuari` VALUES
(1,'Admin','admin@imperium.com','$2a$10$ru5ykaDgdlEaK26H/xAknuW3V/92RRszOFkof0aL8GcdInQk/uNa6','2026-04-16 18:24:35','ADMIN',3,'2026-05-17 21:49:25','2026-06-17 21:49:25',0),
(2,'Joan Garcia','joan@gmail.com','$2a$10$CpPuIpBsxRDkhyyKyAfcg.VMsNqH4yPBUTWYoGBUOGWyHvE8vklpW','2026-04-16 18:24:35','USER',2,'2026-05-14 17:07:38','2026-06-14 17:07:38',0),
(3,'Maria López','maria@gmail.com','$2a$10$ommhLP6snGAotlvt5L3Li.1vfKo5MQUhcPe/8Xv5BO3u8edSTDsvm','2026-04-20 13:10:06','USER',NULL,NULL,NULL,0),
(4,'paco paquito','paco@gmail.com','$2a$10$aA5R/KsWBkwzrNNd/B5IVOt7ItQD7cjgBsby7l2byLj.8YXTqbPcq','2026-04-28 13:22:49','USER',NULL,NULL,NULL,0),
(5,'Joan Garcio','joan7@test.com','$2a$10$ls4jNPxpeaBmagwUmT6YBOwXD9lwgVepnMAwLAxQjFEMVsL56k0YS','2026-05-06 16:57:02','USER',NULL,NULL,NULL,0),
(6,'Les','les@test.com','$2a$10$Bzt226iGiefgDLf2o2F6COA6SSL/A3qoNbK92e/8GWu5X3bb5xTEe','2026-05-11 16:57:14','USER',NULL,NULL,NULL,0),
(7,'les77','les77@test.com','$2a$10$pFsk6t.ET1Gl5XrQFCMEauLWXTXPGnyX80XykCxFoMmUAfR/Knu1S','2026-05-11 17:11:01','USER',NULL,NULL,NULL,0),
(8,'pablo','pablo19@imperium.com','$2a$10$BVhjX667LBB9OpP.MPq.eu5IKqIgUdWQQySRrvW7kwCQQ8.Ie8ggW','2026-05-16 15:20:18','USER',3,'2026-05-17 21:03:09','2026-06-17 21:03:09',0),
(9,'elpeluca','elpeluca@imperium.com','$2a$10$VK7fFBmFZdk2uae.loa8P.XNy6zFpWa0nse5U5hzTqeDFHqGNlPfy','2026-05-17 22:15:57','USER',NULL,NULL,NULL,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
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
(20,3,8,1,'2026-05-16 19:29:48'),
(21,13,1,1,'2026-05-17 16:46:11'),
(22,13,1,1,'2026-05-17 21:48:39'),
(23,12,9,1,'2026-05-17 22:16:42');
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

-- Dump completed on 2026-05-18 17:58:43
