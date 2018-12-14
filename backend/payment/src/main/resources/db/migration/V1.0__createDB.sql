create sequence hibernate_sequence start with 1 increment by 1;
create table if not exists payments (id bigint not null, amount decimal not null, user_id varchar(255), primary key (id));