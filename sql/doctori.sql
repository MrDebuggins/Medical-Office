-- Medical.doctori definition

CREATE TABLE `doctori` (
  `id_doctor` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) DEFAULT NULL,
  `nume` varchar(50) DEFAULT NULL,
  `prenume` varchar(50) DEFAULT NULL,
  `email` varchar(70) DEFAULT NULL,
  `telefon` char(10) DEFAULT NULL,
  `specializare` enum('a','b') DEFAULT NULL,
  PRIMARY KEY (`id_doctor`),
  UNIQUE KEY `doctori_email_UN` (`email`),
  CONSTRAINT `doctori_tel_CHECK` CHECK (`telefon` like '07%')
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
