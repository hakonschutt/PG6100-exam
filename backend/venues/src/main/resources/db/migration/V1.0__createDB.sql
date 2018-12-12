create sequence hibernate_sequence start with 1 increment by 1;

create table if not exists rooms (id bigint not null, columns integer check (columns>=1 AND columns<=80), name varchar(180), rows integer check (rows>=1 AND rows<=80), primary key (id));
create table if not exists venues (id bigint generated by default as identity, address varchar(120), geo_location varchar(300), name varchar(160), primary key (id));
create table if not exists venues_rooms (venues_id bigint not null, rooms_id bigint not null, primary key (venues_id, rooms_id));

alter table venues_rooms add constraint UK_6nijxm1w5owfnviua0vp9186o unique (rooms_id);
alter table venues_rooms add constraint FKakytlv8uduvp5117bji49ybya foreign key (rooms_id) references rooms;
alter table venues_rooms add constraint FK4fd2gmxa0fwrvgpxwd7l3dslt foreign key (venues_id) references venues;