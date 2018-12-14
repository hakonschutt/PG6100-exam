create sequence hibernate_sequence start with 1 increment by 1;

create table if not exists movies (id bigint not null, cover_art varchar(255), overview varchar(255), popularity double, poster varchar(255), price double, release_date date, title varchar(255), trailer varchar(255), vote_average double, vote_count integer, primary key (id));
create table if not exists movies_genres (movies_id bigint not null, genres varchar(255));
alter table movies_genres add constraint FK1i7fpiqqicbfvd33fh0ullmdn foreign key (movies_id) references movies;