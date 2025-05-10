CREATE TABLE IF NOT EXISTS question
(
    id          bigint      not null AUTO_INCREMENT,
    question    varchar(50) not null,
    best_answer varchar(300),
    tag         varchar(50) not null,
    created_at  timestamp(6),
    updated_at  timestamp(6),
    primary key (id)
);


CREATE TABLE IF NOT EXISTS users
(
    id         bigint       not null AUTO_INCREMENT,
    nickname   varchar(20)  not null,
    email      varchar(50)  not null,
    password   varchar(100) not null,
    career     bigint       not null,
    position   varchar(15)  not null,
    role       varchar(10)  not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    primary key (id)
);
