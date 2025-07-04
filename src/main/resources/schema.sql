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

CREATE TABLE IF NOT EXISTS submission
(
    id          bigint       not null AUTO_INCREMENT,
    user_id     bigint       not null,
    question_id bigint       not null,
    answer      varchar(300) not null,
    is_correct  boolean      not null,
    feedback    varchar(1000),
    created_at  timestamp(6),
    updated_at  timestamp(6),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS user_ranking
(
    id                       bigint      not null AUTO_INCREMENT,
    user_id                  bigint      not null,
    ranking                  bigint      not null,
    type                     varchar(20) not null,
    correct_submission_count bigint      not null,
    created_at               timestamp(6),
    updated_at               timestamp(6),
    primary key (id),
    UNIQUE KEY uq_user_type (user_id, type)
);

CREATE TABLE IF NOT EXISTS term
(
    id         bigint       not null AUTO_INCREMENT,
    term       varchar(30)  not null,
    definition varchar(300) not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    primary key (id),
    UNIQUE KEY uq_term (term)
);

CREATE TABLE IF NOT EXISTS contribution
(
    id         bigint       not null AUTO_INCREMENT,
    user_id    bigint       not null,
    count      bigint       not null,
    date       timestamp(6) not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    primary key (id),
    UNIQUE KEY uq_user_date (user_id, date)
);

