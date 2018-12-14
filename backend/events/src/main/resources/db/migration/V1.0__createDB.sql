create sequence hibernate_sequence start with 1 increment by 1;

create table if not exists events (id bigint not null, columns int check (columns>=1 AND columns<=80), date varchar(255), movie_id varchar(255), room_id varchar(255), rows int check (rows>=1 AND rows<=80), venue_id varchar(255), primary key (id));