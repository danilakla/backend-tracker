create table t_user_teacher
(
    id         int primary key,
    c_username varchar not null unique,
    c_password varchar not null
);

create table t_user_student
(
    id         int primary key,
    c_username varchar not null unique,
    c_password varchar not null
);
