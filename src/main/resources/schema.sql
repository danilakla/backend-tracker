-- create table t_user_teacher
-- (
--     id         int primary key,
--     c_username varchar not null unique,
--     c_password varchar not null
-- );
--
-- create table t_user_student
-- (
--     id         int primary key,
--     c_username varchar not null unique,
--     c_password varchar not null
-- );

CREATE TABLE Admins (
    id_admin SERIAL PRIMARY KEY,
    first_name VARCHAR(30),
    last_name VARCHAR(30)
);

CREATE TABLE Universities (
    id_university SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    id_admin INT,
    FOREIGN KEY (id_admin) REFERENCES Admins(id_admin)
);

CREATE TABLE Deans (
    id_dean SERIAL PRIMARY KEY,
    last_name VARCHAR(50),
    first_name VARCHAR(50),
    patronymic VARCHAR(50),
    faculty VARCHAR(70),
    id_university INT,
    FOREIGN KEY (id_university) REFERENCES Universities(id_university)
);

CREATE TABLE Specialties (
    id_specialty SERIAL PRIMARY KEY,
    name VARCHAR(80),
    id_dean INT,
    FOREIGN KEY (id_dean) REFERENCES Deans(id_dean)
);

CREATE TABLE Disciplines (
    id_discipline SERIAL PRIMARY KEY,
    id_dean INT,
    name VARCHAR(100),
    FOREIGN KEY (id_dean) REFERENCES Deans(id_dean)
);

CREATE TABLE ClassFormats (
    id_class_format SERIAL PRIMARY KEY,
    format_name VARCHAR(100),
    description TEXT,
    id_dean INT,
    FOREIGN KEY (id_dean) REFERENCES Deans(id_dean)
);

CREATE TABLE Teachers (
    id_teacher SERIAL PRIMARY KEY,
    id_university INT,
    last_name VARCHAR(50),
    first_name VARCHAR(50),
    patronymic VARCHAR(50),
    FOREIGN KEY (id_university) REFERENCES Universities(id_university)
);

CREATE TABLE Subgroups (
    id_subgroup SERIAL PRIMARY KEY,
    subgroup_number INT,
    admission_date DATE,
    id_dean INT,
    id_specialty INT,
    FOREIGN KEY (id_dean) REFERENCES Deans(id_dean),
    FOREIGN KEY (id_specialty) REFERENCES Specialties(id_specialty)
);

CREATE TABLE Students (
    id_student SERIAL PRIMARY KEY,
    id_subgroup INT,
    last_name VARCHAR(50),
    first_name VARCHAR(50),
    patronymic VARCHAR(50),
    login VARCHAR(50),
    password VARCHAR(50),
    key_student_parents TEXT,
    FOREIGN KEY (id_subgroup) REFERENCES Subgroups(id_subgroup)
);

CREATE TABLE Subjects (
    id_subject SERIAL PRIMARY KEY,
    id_discipline INT,
    id_teacher INT,
    FOREIGN KEY (id_discipline) REFERENCES Disciplines(id_discipline),
    FOREIGN KEY (id_teacher) REFERENCES Teachers(id_teacher)
);

CREATE TABLE ClassGroups (
    id_class_group SERIAL PRIMARY KEY,
    id_subject INT,
    description TEXT,
    quantity INT,
    id_class_format INT,
    id_teacher INT,
    FOREIGN KEY (id_subject) REFERENCES Subjects(id_subject),
    FOREIGN KEY (id_class_format) REFERENCES ClassFormats(id_class_format),
    FOREIGN KEY (id_teacher) REFERENCES Teachers(id_teacher)
);

CREATE TABLE Classes (
    id_class SERIAL PRIMARY KEY,
    id_class_group INT,
    FOREIGN KEY (id_class_group) REFERENCES ClassGroups(id_class_group)
);

CREATE TABLE StudentGrades (
    id_student INT,
    id_class INT,
    grade INT,
    description TEXT,
    attendance BOOLEAN,
    PRIMARY KEY (id_student, id_class),
    FOREIGN KEY (id_student) REFERENCES Students(id_student),
    FOREIGN KEY (id_class) REFERENCES Classes(id_class)
);

CREATE TABLE ClassGroupsToSubgroups (
    id_subgroup INT,
    id_class_group INT,
    PRIMARY KEY (id_subgroup, id_class_group),
    FOREIGN KEY (id_subgroup) REFERENCES Subgroups(id_subgroup),
    FOREIGN KEY (id_class_group) REFERENCES ClassGroups(id_class_group)
);