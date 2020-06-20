create schema toby_spring_learning charset utf8;

create table users (
  id varchar(10) primary key,
  name varchar(20) not null,
  password varchar(10) not null,
  level tinyint not null,
  login int not null,
  recommend int not null
)

create schema testdb charset utf8;

create table testdb.users (
  id varchar(10) primary key,
  name varchar(20) not null,
  password varchar(10) not null,
  level tinyint not null,
  login int not null,
  recommend int not null
)

