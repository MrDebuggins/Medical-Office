-- Medical.pacienti definition

CREATE TABLE `pacienti` (
  `CNP` char(13) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `nume` varchar(50) NOT NULL,
  `prenume` varchar(50) DEFAULT NULL,
  `email` varchar(70) DEFAULT NULL,
  `telefon` varchar(10) DEFAULT NULL,
  `nascut` date DEFAULT NULL,
  `activ` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`CNP`),
  UNIQUE KEY `email_UN` (`email`),
  CONSTRAINT `Pacients_tel_CHECK` CHECK (`telefon` like '07%')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DELIMITER //

CREATE DEFINER=`root`@`%` TRIGGER patient_18_yo
BEFORE INSERT ON pacienti
FOR EACH ROW
BEGIN
	IF (NEW.nascut > (NOW() - INTERVAL 18 YEAR)) THEN
	SIGNAL SQLSTATE  '45000' set MESSAGE_TEXT = 'Patient should be at least 18 yo';
	END IF;
END;

//

insert into pacienti values('1111', 5, 'patient1', 'patient1', 'patient1@mail.com', '0770123456', '2000-02-10', true);
insert into pacienti values('2222', 6, 'patient2', 'patient2', 'patient2@mail.com', '0770123456', '2000-02-10', true);
insert into pacienti values('3333', 7, 'patient3', 'patient3', 'patient3@mail.com', '0770123456', '2000-02-10', true);
insert into pacienti values('4444', 8, 'patient4', 'patient4', 'patient4@mail.com', '0770123456', '2000-02-10', true);