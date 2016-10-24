-- MySQL dump 10.13  Distrib 5.7.15, for Linux (x86_64)
--
-- Host: localhost    Database: metadata_ddp
-- ------------------------------------------------------
-- Server version	5.7.15-0ubuntu0.16.04.1

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
-- Table structure for table `connectionlinkage`
--

DROP TABLE IF EXISTS `connectionlinkage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `connectionlinkage` (
  `connectionlinkage_id` int(11) NOT NULL AUTO_INCREMENT,
  `connection_id` int(11) DEFAULT NULL,
  `datasource_id` int(11) DEFAULT NULL,
  `dataentity_id` int(11) DEFAULT NULL,
  `datafield_id` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`connectionlinkage_id`),
  KEY `fk_connectionlinkage_1_idx` (`connection_id`),
  KEY `fk_connectionlinkage_2_idx` (`dataentity_id`),
  KEY `fk_connectionlinkage_3_idx` (`datafield_id`),
  KEY `fk_connectionlinkage_4_idx` (`datasource_id`),
  CONSTRAINT `fk_connectionlinkage_1` FOREIGN KEY (`connection_id`) REFERENCES `connections` (`connection_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_connectionlinkage_2` FOREIGN KEY (`dataentity_id`) REFERENCES `dataentity` (`dataentity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_connectionlinkage_3` FOREIGN KEY (`datafield_id`) REFERENCES `datafield` (`datafield_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_connectionlinkage_4` FOREIGN KEY (`datasource_id`) REFERENCES `datasource` (`datasource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `connectionlinkage`
--

LOCK TABLES `connectionlinkage` WRITE;
/*!40000 ALTER TABLE `connectionlinkage` DISABLE KEYS */;
INSERT INTO `connectionlinkage` VALUES (1,1,1,1,1,NULL,NULL);
/*!40000 ALTER TABLE `connectionlinkage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `connections`
--

DROP TABLE IF EXISTS `connections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `connections` (
  `connection_id` int(11) NOT NULL AUTO_INCREMENT,
  `connection_name` varchar(255) NOT NULL,
  `owner` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`connection_id`),
  KEY `fk_connections_1_idx` (`owner`),
  CONSTRAINT `fk_connections_1` FOREIGN KEY (`owner`) REFERENCES `subject` (`subject_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `connections`
--

LOCK TABLES `connections` WRITE;
/*!40000 ALTER TABLE `connections` DISABLE KEYS */;
INSERT INTO `connections` VALUES (1,'my-spark-app',1,'spark://t440:7077',NULL,NULL);
/*!40000 ALTER TABLE `connections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dataentity`
--

DROP TABLE IF EXISTS `dataentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dataentity` (
  `dataentity_id` int(11) NOT NULL AUTO_INCREMENT,
  `dataentity_name` varchar(45) NOT NULL,
  `datasource_id` int(11) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dataentity_id`),
  KEY `fk_dataentity_1_idx` (`datasource_id`),
  CONSTRAINT `fk_dataentity_1` FOREIGN KEY (`datasource_id`) REFERENCES `datasource` (`datasource_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dataentity`
--

LOCK TABLES `dataentity` WRITE;
/*!40000 ALTER TABLE `dataentity` DISABLE KEYS */;
INSERT INTO `dataentity` VALUES (1,'transaction',1,NULL,NULL,NULL),(2,'address',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `dataentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datafield`
--

DROP TABLE IF EXISTS `datafield`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datafield` (
  `datafield_id` int(11) NOT NULL AUTO_INCREMENT,
  `datafield_name` varchar(45) NOT NULL,
  `datatype` varchar(45) DEFAULT NULL,
  `dataentity_id` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `comments` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`datafield_id`),
  KEY `fk_datafield_1_idx` (`dataentity_id`),
  CONSTRAINT `fk_datafield_1` FOREIGN KEY (`dataentity_id`) REFERENCES `dataentity` (`dataentity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datafield`
--

LOCK TABLES `datafield` WRITE;
/*!40000 ALTER TABLE `datafield` DISABLE KEYS */;
INSERT INTO `datafield` VALUES (1,'name','string',1,NULL,NULL,NULL),(2,'address','string',1,NULL,NULL,NULL),(3,'age','int',1,NULL,NULL,NULL),(4,'name','string',2,NULL,NULL,NULL),(5,'amount','decimal',2,NULL,NULL,NULL),(6,'cardno','string',2,NULL,NULL,NULL);
/*!40000 ALTER TABLE `datafield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datasource`
--

DROP TABLE IF EXISTS `datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datasource` (
  `datasource_id` int(11) NOT NULL AUTO_INCREMENT,
  `datasource_name` varchar(45) DEFAULT NULL,
  `lob` varchar(45) DEFAULT NULL,
  `desciption` varchar(45) DEFAULT NULL,
  `owner` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `lob_group` int(11) DEFAULT NULL,
  PRIMARY KEY (`datasource_id`),
  UNIQUE KEY `desciption_UNIQUE` (`desciption`),
  KEY `fk_datasource_1_idx` (`owner`,`lob_group`),
  KEY `fk_datasource_2_idx` (`lob_group`),
  CONSTRAINT `fk_datasource_1` FOREIGN KEY (`owner`) REFERENCES `subject` (`subject_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_datasource_2` FOREIGN KEY (`lob_group`) REFERENCES `subject` (`subject_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datasource`
--

LOCK TABLES `datasource` WRITE;
/*!40000 ALTER TABLE `datasource` DISABLE KEYS */;
INSERT INTO `datasource` VALUES (1,'cif','cif','cif',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject` (
  `subject_id` int(11) NOT NULL,
  `subject_name` varchar(45) NOT NULL,
  `subject_type_id` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`subject_id`),
  KEY `fk_subject_1_idx` (`subject_type_id`),
  CONSTRAINT `fk_subject_1` FOREIGN KEY (`subject_type_id`) REFERENCES `subjecttype` (`subjecttype_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES (1,'guoe2',1,NULL,NULL);
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjecttype`
--

DROP TABLE IF EXISTS `subjecttype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subjecttype` (
  `subjecttype_id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_type_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`subjecttype_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjecttype`
--

LOCK TABLES `subjecttype` WRITE;
/*!40000 ALTER TABLE `subjecttype` DISABLE KEYS */;
INSERT INTO `subjecttype` VALUES (1,'acf2id'),(2,'adgroup');
/*!40000 ALTER TABLE `subjecttype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `view_hierarchy`
--

DROP TABLE IF EXISTS `view_hierarchy`;
/*!50001 DROP VIEW IF EXISTS `view_hierarchy`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `view_hierarchy` AS SELECT 
 1 AS `datasource_id`,
 1 AS `datasource_name`,
 1 AS `dataentity_id`,
 1 AS `dataentity_name`,
 1 AS `datafield_id`,
 1 AS `datafield_name`,
 1 AS `datatype`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_hierarchy`
--

/*!50001 DROP VIEW IF EXISTS `view_hierarchy`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_hierarchy` AS select `s`.`datasource_id` AS `datasource_id`,`s`.`datasource_name` AS `datasource_name`,`e`.`dataentity_id` AS `dataentity_id`,`e`.`dataentity_name` AS `dataentity_name`,`f`.`datafield_id` AS `datafield_id`,`f`.`datafield_name` AS `datafield_name`,`f`.`datatype` AS `datatype` from ((`datasource` `s` join `dataentity` `e`) join `datafield` `f`) where ((isnull(`s`.`start_date`) or (`s`.`start_date` <= now())) and (isnull(`s`.`end_date`) or (`s`.`end_date` >= now())) and (isnull(`e`.`start_date`) or (`e`.`start_date` <= now())) and (isnull(`e`.`end_date`) or (`e`.`end_date` >= now())) and (isnull(`f`.`start_date`) or (`f`.`start_date` <= now())) and (isnull(`f`.`end_date`) or (`f`.`end_date` >= now())) and (`s`.`datasource_id` = `e`.`datasource_id`) and (`e`.`dataentity_id` = `f`.`dataentity_id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-10-23 21:26:28
