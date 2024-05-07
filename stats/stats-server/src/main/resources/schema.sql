create table if not exists hits (
    id bigint generated by default as identity primary key,
    app varchar(255) not null,
    uri varchar(255) not null,
    ip varchar(15) not null,
    time_stamp timestamp without time zone not null
);