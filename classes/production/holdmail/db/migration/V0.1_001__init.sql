
create table message (
  message_id int auto_increment primary key,
  identifier varchar(100) not null,
  subject varchar(255) not null,
  sender_email varchar(255) not null,
  received_date timestamp not null,
  sender_host varchar(255),
  message_size int not null,
  message_body text not null
);

create table message_header (
  message_header_id int auto_increment primary key,
  message_id int not null,
  header_name varchar(255) not null,
  header_value varchar(1000),
  foreign key (message_id) references message(message_id)
);

create table message_recipient (
  message_recipient_id int auto_increment primary key,
  message_id int not null,
  recipient_email varchar(255) not null,
  foreign key (message_id) references message(message_id)
 );

