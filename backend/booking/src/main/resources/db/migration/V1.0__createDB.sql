create sequence hibernate_sequence start with 1 increment by 1;

create table if not exists booking_tickets (ticket_id bigint not null, tickets_id bigint not null, primary key (ticket_id, tickets_id));
create table if not exists bookings (id bigint not null, event bigint, user bigint, primary key (id));
create table if not exists tickets (id bigint not null, price double, seat varchar(255), primary key (id));

alter table booking_tickets add constraint UK_nh7rjni8fxuedp5qa0h0dsbhf unique (tickets_id);
alter table booking_tickets add constraint FKp9o3biuddwrpdfpn9ffmc5f2v foreign key (tickets_id) references tickets;
alter table booking_tickets add constraint FK27wpxhtug19kdmaeog2rhx6da foreign key (ticket_id) references bookings;
