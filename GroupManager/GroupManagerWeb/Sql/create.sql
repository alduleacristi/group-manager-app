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

insert into gm_user(email,firstname,lastname,password) values ("alduleacristi@yahoo.com","Cristian-Ionel","Aldulea","1234")