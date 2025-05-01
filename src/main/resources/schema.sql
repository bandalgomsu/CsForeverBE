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
    id         bigint                        not null AUTO_INCREMENT,
    nickname   varchar(20)                   not null,
    email      varchar(50)                   not null,
    password   varchar(50)                   not null,
    role       Enum ('USER','GUEST','ADMIN') not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    primary key (id)
);
