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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

insert into doctori values(1, 1, 'doctor1', 'doctor1', 'doctor1@mail.com', '0770132456', 'a');
insert into doctori values(2, 2, 'doctor2', 'doctor2', 'doctor2@mail.com', '0770132456', 'a');
insert into doctori values(3, 3, 'doctor3', 'doctor3', 'doctor3@mail.com', '0770132456', 'b');
insert into doctori values(4, 4, 'doctor4', 'doctor4', 'doctor4@mail.com', '0770132456', 'b');