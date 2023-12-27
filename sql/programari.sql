-- Medical.programari definition

CREATE TABLE `programari` (
  `id_pacient` char(13) NOT NULL,
  `id_doctor` int(11) NOT NULL,
  `p_data` datetime NOT NULL,
  `status` enum('onorat','neprezentat','anulat') DEFAULT NULL,
  PRIMARY KEY (`id_pacient`,`id_doctor`,`p_data`),
  KEY `programari_FK` (`id_doctor`),
  CONSTRAINT `programari_FK` FOREIGN KEY (`id_doctor`) REFERENCES `doctori` (`id_doctor`),
  CONSTRAINT `programari_FK_1` FOREIGN KEY (`id_pacient`) REFERENCES `pacienti` (`CNP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DELIMITER //
CREATE TRIGGER programari_overlap
BEFORE INSERT ON programari
FOR EACH ROW
BEGIN
	IF 0 <> (select count(id_pacient) from programari p 
			where p.id_doctor = new.id_doctor and
				p.id_pacient = new.id_pacient and
				p.p_data.date() = new.p_data.date()) THEN
	SIGNAL SQLSTATE  '45000' set MESSAGE_TEXT = 'Cannot have two appointments to the same doctor in the same day';
	END IF;

	if 15 > (select count(id_doctor) from programari p
			where (p.id_doctor = new.id_doctor or p.id_pacient = new.id_pacient) and 
			(p.p_data.date() = new.p_data.date()) and minute(TIMEDIFF(p.p_data, new.p_data))) then
	SIGNAL sqlstate '45000' set MESSAGE_TEXT = 'Appointment is overlapping with another one';
	end if;
END;

CREATE DEFINER=`root`@`%` TRIGGER programari_delete
BEFORE DELETE ON programari
FOR EACH ROW
BEGIN
	IF (old.p_data < NOW()) THEN
	SIGNAL SQLSTATE  '45000' set MESSAGE_TEXT = 'Cannot delete appointment from past';
	END IF;
END;
//
