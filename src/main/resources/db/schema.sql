drop table if exists users;

create table  users (
    id varchar(10) primary key,
    name varchar(20) not null,
    password varchar(10) not null,
    level int not null,
    login varchar(20) not null,
    recommend int not null
);