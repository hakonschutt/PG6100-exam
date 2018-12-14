create sequence hibernate_sequence start with 1 increment by 1;

create table if not exists booking_tickets (ticket_id bigint not null, tickets_id bigint not null, primary key (ticket_id, tickets_id));
create table if not exists bookings (id bigint not null, event_id bigint, user_id bigint, primary key (id));
create table if not exists tickets (id bigint not null, price decimal, seat varchar(255), primary key (id));

alter table booking_tickets add constraint UK_nh7rasdfdsafadjni8fxuedp5qa0h0dsbhf unique (tickets_id);
alter table booking_tickets add constraint FKp9o3biasfdsafauddwrpdfpn9ffmc5f2v foreign key (tickets_id) references tickets;
alter table booking_tickets add constraint FK27wpxhasdfsadftug19kdmaeog2rhx6da foreign key (ticket_id) references bookings;
insert into bookings (event_id, user_id, id) values (1, 1, 1 );
insert into tickets (price, seat, id) values (10, '3', 1 );
insert into booking_tickets (ticket_id, tickets_id) values (1, 1);
insert into bookings (event_id, user_id, id) values (2, 2, 2);
insert into tickets (price, seat, id) values (10, '3', 2 );
insert into booking_tickets (ticket_id, tickets_id) values (2, 2);
