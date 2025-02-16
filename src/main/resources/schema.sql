CREATE TABLE UserRoles
(
    id_role   SERIAL PRIMARY KEY,
    role_name varchar(50)
);

INSERT INTO UserRoles (role_name)
VALUES ('ADMIN'),
       ('STUDENT'),
       ('TEACHER'),
       ('DEAN');

CREATE TABLE UserAccounts
(
    id_account SERIAL PRIMARY KEY,
    login      VARCHAR(50) unique,
    password   VARCHAR(250),
    id_role    INT,
    FOREIGN KEY (id_role) REFERENCES UserRoles (id_role)
);

CREATE TABLE Admins
(
    id_admin   SERIAL PRIMARY KEY,
    flp_name   VARCHAR(200),
    id_account INT,
    FOREIGN KEY (id_account) REFERENCES UserAccounts (id_account)
);

CREATE TABLE Universities
(
    id_university SERIAL PRIMARY KEY,
    name          VARCHAR(100),
    description   TEXT,
    id_admin      INT,
    FOREIGN KEY (id_admin) REFERENCES Admins (id_admin)

);

CREATE TABLE Deans
(
    id_dean       SERIAL PRIMARY KEY,
    flp_name      VARCHAR(200),
    faculty       VARCHAR(70),
    id_university INT,
    id_account    INT,
    FOREIGN KEY (id_university) REFERENCES Universities (id_university),
    FOREIGN KEY (id_account) REFERENCES UserAccounts (id_account)

);

CREATE TABLE Specialties
(
    id_specialty SERIAL PRIMARY KEY,
    name         VARCHAR(80),
    id_dean      INT,
    FOREIGN KEY (id_dean) REFERENCES Deans (id_dean)
);

CREATE TABLE Disciplines
(
    id_discipline SERIAL PRIMARY KEY,
    id_dean       INT,
    name          VARCHAR(100),
    FOREIGN KEY (id_dean) REFERENCES Deans (id_dean)
);

CREATE TABLE ClassFormats
(
    id_class_format SERIAL PRIMARY KEY,
    format_name     VARCHAR(100),
    description     TEXT,
    id_dean         INT,
    FOREIGN KEY (id_dean) REFERENCES Deans (id_dean)
);

CREATE TABLE Teachers
(
    id_teacher    SERIAL PRIMARY KEY,
    id_university INT,
    flp_name      VARCHAR(200),
    id_account    INT,
    FOREIGN KEY (id_university) REFERENCES Universities (id_university),
    FOREIGN KEY (id_account) REFERENCES UserAccounts (id_account)
);

CREATE TABLE Subgroups
(
    id_subgroup     SERIAL PRIMARY KEY,
    subgroup_number VARCHAR(50),
    admission_date  DATE,
    id_dean         INT,
    id_specialty    INT,
    FOREIGN KEY (id_dean) REFERENCES Deans (id_dean),
    FOREIGN KEY (id_specialty) REFERENCES Specialties (id_specialty)
);

CREATE TABLE Students
(
    id_student          SERIAL PRIMARY KEY,
    id_subgroup         INT,
    flp_name            VARCHAR(200),
    key_student_parents TEXT,
    id_account          INT,
    FOREIGN KEY (id_subgroup) REFERENCES Subgroups (id_subgroup) ON DELETE CASCADE,
    FOREIGN KEY (id_account) REFERENCES UserAccounts (id_account) ON DELETE CASCADE
);

CREATE TABLE Subjects
(
    id_subject  SERIAL PRIMARY KEY,
    id_dean     INT,
    name        VARCHAR(100),
    description VARCHAR(500),
    FOREIGN KEY (id_dean) REFERENCES Deans (id_dean)
);

CREATE TABLE ClassGroups
(
    id_class_group  SERIAL PRIMARY KEY,
    id_subject      INT,
    description     TEXT,
    id_class_format INT,
    id_teacher      INT,
    id_dean         INT,
    FOREIGN KEY (id_dean) REFERENCES Deans (id_dean),
    FOREIGN KEY (id_subject) REFERENCES Subjects (id_subject) ON DELETE CASCADE,
    FOREIGN KEY (id_class_format) REFERENCES ClassFormats (id_class_format),
    FOREIGN KEY (id_teacher) REFERENCES Teachers (id_teacher)
);
create table ClassGroupsHold
(

    id_class_hold SERIAL PRIMARY KEY,
    has_apply_attestation BOOLEAN default true
);

CREATE TABLE ClassGroupsToSubgroups
(
    id_class_group_to_subgroup SERIAL PRIMARY KEY,
    id_subgroup                INT,
    id_class_group             INT,
    id_class_hold              INT,
    FOREIGN KEY (id_subgroup) REFERENCES Subgroups (id_subgroup) ON DELETE CASCADE,
    FOREIGN KEY (id_class_group) REFERENCES ClassGroups (id_class_group) ON DELETE CASCADE,
    FOREIGN KEY (id_class_hold) REFERENCES ClassGroupsHold (id_class_hold) ON DELETE CASCADE,
    UNIQUE (id_subgroup, id_class_group)
);


CREATE TABLE Classes
(
    id_class      SERIAL PRIMARY KEY,
    id_class_hold INT,
    date_creation DATE DEFAULT CURRENT_DATE,
    is_attestation boolean default false,
    FOREIGN KEY (id_class_hold) REFERENCES ClassGroupsHold (id_class_hold) ON DELETE CASCADE
);

CREATE TABLE StudentGrades
(
    id_student_grate SERIAL PRIMARY KEY,

    id_student       INT,
    id_class         INT,
    grade            INT DEFAULT 0,
    description      TEXT,
    attendance       INT,
    is_pass_lab boolean default false,

    FOREIGN KEY (id_student) REFERENCES Students (id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_class) REFERENCES Classes (id_class) ON DELETE CASCADE
);
CREATE TABLE AttestationStudentGrades
(
    id_attestation_student_grades SERIAL PRIMARY KEY,

    id_student                    INT,
    id_class                      INT,
    avg_grade                     double precision,
    hour                          double precision,
    current_count_lab             INT,
    max_count_lab                 INT,
    is_attested BOOLEAN default false,
    FOREIGN KEY (id_student) REFERENCES Students (id_student) ON DELETE CASCADE,
    FOREIGN KEY (id_class) REFERENCES Classes (id_class) ON DELETE CASCADE
);


CREATE INDEX idx_cgts_class_hold ON ClassGroupsToSubgroups(id_class_hold);
CREATE INDEX idx_students_subgroup ON Students(id_subgroup);