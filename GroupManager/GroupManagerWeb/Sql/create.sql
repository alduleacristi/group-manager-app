CREATE SCHEMA `groupmanagerservices` ;
use groupmanagerservices;

create table gm_user_group_map(
	user_id bigint(20) not null,
	group_id bigint(20) not null
);

create table gm_user(
	user_id bigint primary key auto_increment,
	email varchar(50) not null,
	firstname varchar(50),
	lastname varchar(50),
	password varchar(50) not null
);

DROP TABLE IF EXISTS `gm_group`;
CREATE TABLE `gm_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`),
  KEY `FK_r9pmnbbq7dc36oxpwknsx5p68` (`user_id`),
  CONSTRAINT `FK_r9pmnbbq7dc36oxpwknsx5p68` FOREIGN KEY (`user_id`) REFERENCES `gm_user` (`user_id`)
);

DROP TABLE IF EXISTS `gm_position`;
CREATE TABLE `gm_position` (
  `position_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `x_coordonate` double DEFAULT NULL,
  `y_coordonate` double DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`position_id`),
  KEY `FK_mf9u0b59vm04tlfxp1hj99n53` (`user_id`),
  CONSTRAINT `FK_mf9u0b59vm04tlfxp1hj99n53` FOREIGN KEY (`user_id`) REFERENCES `gm_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


insert into gm_user(email,firstname,lastname,password) values ("alduleacristi@yahoo.com","Cristian-Ionel","Aldulea","1234");
insert into gm_user(email,firstname,lastname,password) values ("ionvasile@yahoo.com","Ion","Vasile","1234");
insert into gm_user(email,firstname,lastname,password) values ("mihaipopa@yahoo.com","Mihai","Popa","1234");
insert into gm_user(email,firstname,lastname,password) values ("razvanpopescu","Razvan","Popescu","1234");

insert into gm_group(name,user_id) values ("TestGroup",1);

insert into gm_user_group_map(user_id,group_id) values (1,1);
insert into gm_user_group_map(user_id,group_id) values (2,1);
insert into gm_user_group_map(user_id,group_id) values (3,1);
insert into gm_user_group_map(user_id,group_id) values (4,1);

insert into gm_position(x_coordonate,y_coordonate,user_id) values (45.1234,15.1234,1);
insert into gm_position(x_coordonate,y_coordonate,user_id) values (45.2234,15.3234,2);
insert into gm_position(x_coordonate,y_coordonate,user_id) values (45.3234,15.3234,3);
insert into gm_position(x_coordonate,y_coordonate,user_id) values (45.4234,15.4234,4);