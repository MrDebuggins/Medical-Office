-- `User`.Users definition

CREATE TABLE `Users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` int(11) NOT NULL DEFAULT 2,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

insert into Users values(9, 'admin', 'admin', 0);

insert into Users values(1, 'doctor1', 'doctor1', 1);
insert into Users values(2, 'doctor2', 'doctor2', 1);
insert into Users values(3, 'doctor3', 'doctor3', 1);
insert into Users values(4, 'doctor4', 'doctor4', 1);

insert into Users values(5, 'patient1', 'patient1', 2);
insert into Users values(6, 'patient2', 'patient2', 2);
insert into Users values(7, 'patient3', 'patient3', 2);
insert into Users values(8, 'patient4', 'patient4', 2);