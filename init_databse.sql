/* -------------------------------------------------------------------------- */
/*  Run this in MariaDB 10.5+ (or MySQL 8+)                                   */
/* -------------------------------------------------------------------------- */

DROP DATABASE IF EXISTS proiect_pao;
CREATE DATABASE proiect_pao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE proiect_pao;

/* -------------------------------------------------------------------------- */
/*  PERSONS – base class for Medic & Patient                                  */
/* -------------------------------------------------------------------------- */
CREATE TABLE persons (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    first_name       VARCHAR(100)  NOT NULL,
    last_name        VARCHAR(100)  NOT NULL,
    age              INT,
    birth            DATE,
    phone_number     VARCHAR(30),
    email            VARCHAR(150),
    address          VARCHAR(255)
);

/* -------------------------------------------------------------------------- */
/*  CALENDARS – each Medic can point here via calendar_id                     */
/* -------------------------------------------------------------------------- */
CREATE TABLE calendars (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    start_hour       INT        NOT NULL,     -- e.g. 8  = 08:00
    end_hour         INT        NOT NULL,     -- e.g. 17 = 17:00
    free_days        VARCHAR(255),            -- CSV of day‐of‐year ints
    appointed_dates  TEXT                    -- "ISO_DATETIME#duration;…"
);

/* -------------------------------------------------------------------------- */
/*  MEDICS – one-to-one with persons, now includes calendar_id                */
/* -------------------------------------------------------------------------- */
CREATE TABLE medics (
    id                   INT PRIMARY KEY,     -- FK = persons.id
    specialization       VARCHAR(100),
    years_experience     INT,
    calendar_id          INT UNIQUE,
    CONSTRAINT fk_medics_person
        FOREIGN KEY (id) REFERENCES persons(id) ON DELETE CASCADE,
    CONSTRAINT fk_medics_calendar
        FOREIGN KEY (calendar_id) REFERENCES calendars(id) ON DELETE SET NULL
);

/* -------------------------------------------------------------------------- */
/*  PATIENTS – one-to-one with persons                                        */
/* -------------------------------------------------------------------------- */
CREATE TABLE patients (
    id           INT PRIMARY KEY,             -- FK = persons.id
    insured      BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_patients_person
        FOREIGN KEY (id) REFERENCES persons(id) ON DELETE CASCADE
);

/* -------------------------------------------------------------------------- */
/*  CLINIQUES                                                                 */
/* -------------------------------------------------------------------------- */
CREATE TABLE cliniques (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(150) NOT NULL,
    address   VARCHAR(255) NOT NULL
);

/* -------------------------------------------------------------------------- */
/*  ROOMS (each belongs to a Clinique)                                        */
/* -------------------------------------------------------------------------- */
CREATE TABLE rooms (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    clinique_id  INT NOT NULL,
    room_type    VARCHAR(50),
    floor        INT,
    room_number  INT,
    CONSTRAINT fk_rooms_clinique
        FOREIGN KEY (clinique_id) REFERENCES cliniques(id) ON DELETE CASCADE
);

/* -------------------------------------------------------------------------- */
/*  DISEASES                                                                  */
/* -------------------------------------------------------------------------- */
CREATE TABLE diseases (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(150) NOT NULL,
    type      VARCHAR(50)  -- e.g. VIRAL, BACTERIAL …
);

/* -------------------------------------------------------------------------- */
/*  MED_FILES (1-to-1 with Patient)                                           */
/* -------------------------------------------------------------------------- */
CREATE TABLE med_files (
    id                 INT AUTO_INCREMENT PRIMARY KEY,
    patient_id         INT NOT NULL UNIQUE,
    registration_date  DATE,
    CONSTRAINT fk_med_files_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

/* -------------------------------------------------------------------------- */
/*  APPOINTMENTS                                                              */
/* -------------------------------------------------------------------------- */
CREATE TABLE appointments (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    medic_id          INT NOT NULL,
    patient_id        INT,
    date_time         DATETIME NOT NULL,
    duration_minutes  INT,
    type              VARCHAR(50),
    disease_id        INT,
    cost              DOUBLE,
    clinique_id       INT,
    room_number       INT,
    CONSTRAINT fk_appt_medic     FOREIGN KEY (medic_id)   REFERENCES medics(id),
    CONSTRAINT fk_appt_patient   FOREIGN KEY (patient_id)  REFERENCES patients(id),
    CONSTRAINT fk_appt_disease   FOREIGN KEY (disease_id) REFERENCES diseases(id),
    CONSTRAINT fk_appt_clinique  FOREIGN KEY (clinique_id)REFERENCES cliniques(id)
);

/* -------------------------------------------------------------------------- */
/*  SAMPLE DATA                                                               */
/* -------------------------------------------------------------------------- */

-- 1) PERSONS (6 rows)
INSERT INTO persons (id, first_name, last_name, age, birth, phone_number, email, address) VALUES
  (1, 'John',   'Doe',    45, '1978-05-01', '0712345678', 'john.doe@med.com',   'Str. Sanatatii 1'),
  (2, 'Anna',   'Smith',  32, '1991-08-15', '0722334455', 'anna.smith@mail.com', 'Str. Florilor 10'),
  (3, 'Bob',    'Brown',  50, '1973-02-20', '0733455667', 'bob.brown@med.com',   'Str. Libertatii 5'),
  (4, 'Carol',  'White',  28, '1995-11-02', '0744566778', 'carol.white@mail.com','Str. Paltinis 12'),
  (5, 'Dave',   'Green',  60, '1963-07-30', '0755677889', 'dave.green@med.com',  'Str. Unirii 20'),
  (6, 'Eve',    'Black',  37, '1986-03-18', '0766788990', 'eve.black@mail.com',  'Str. Primaverii 8');

-- 2) CALENDARS (3 rows)
INSERT INTO calendars (id, start_hour, end_hour, free_days, appointed_dates) VALUES
  (1, 8,  17, '1,50,100',  '2025-06-06T10:30:00#30;2025-06-07T11:00:00#45'),
  (2, 9,  18, '10,20,30',  '2025-07-01T09:00:00#60'),
  (3, 7,  15, '5,15,25,35','2025-08-15T11:15:00#45;2025-08-16T14:00:00#30');

-- 3) MEDICS (3 rows)
INSERT INTO medics (id, specialization, years_experience, calendar_id) VALUES
  (1, 'CARDIOLOGY', 20, 1),
  (3, 'ORTHOPEDICS', 15, 2),
  (5, 'NEUROLOGY',   25, 3);

-- 4) PATIENTS (3 rows)
INSERT INTO patients (id, insured) VALUES
  (2, TRUE),
  (4, FALSE),
  (6, TRUE);

-- 5) CLINIQUES (3 rows)
INSERT INTO cliniques (id, name,                 address) VALUES
  (1, 'Central Clinique',    'Bucharest, Bd. Independentei 99'),
  (2, 'Eastside Clinic',     'Bucharest, Str. Lalelelor 15'),
  (3, 'Westside Medical',    'Cluj-Napoca, Str. Napoca 30');

-- 6) ROOMS (at least 3 rows)
INSERT INTO rooms (id, clinique_id, room_type,      floor, room_number) VALUES
  (1, 1,           'CONSULTATION', 1,     101),
  (2, 1,           'OPERATING',    2,     202),
  (3, 2,           'CONSULTATION', 1,     110),
  (4, 3,           'EMERGENCY',    0,     001);

-- 7) DISEASES (3 rows)
INSERT INTO diseases (id, name,          type) VALUES
  (1, 'Hypertension', 'CHRONIC'),
  (2, 'Influenza',    'VIRAL'),
  (3, 'Fracture',     'TRAUMA');

-- 8) MED_FILES (3 rows)
INSERT INTO med_files (id, patient_id, registration_date) VALUES
  (1, 2, '2024-01-10'),
  (2, 4, '2024-03-22'),
  (3, 6, '2024-05-05');

-- 9) APPOINTMENTS (3 rows)
INSERT INTO appointments (id, medic_id, patient_id, date_time,           duration_minutes, type,        disease_id, cost,   clinique_id, room_number) VALUES
  (1, 1,        2,          '2025-06-06 10:30:00', 30,               'CHECKUP',    1,          250.00, 1,           101),
  (2, 3,        4,          '2025-07-01 09:00:00', 60,               'FOLLOW_UP',  2,          150.00, 2,           110),
  (3, 5,        6,          '2025-08-15 11:15:00', 45,               'SURGERY',    3,         1200.00, 3,            1);
select * from medics;