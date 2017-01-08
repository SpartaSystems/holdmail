
-- github issue 8 / add millisecond resolution
alter table message modify received_date timestamp(3) not null;

