/* -------------------------------------------------------------------------- */
/*  Run this in MariaDB 10.5+ (or MySQL 8+)                                   */
/* -------------------------------------------------------------------------- */

DROP DATABASE IF EXISTS proiect_pao;
CREATE DATABASE proiect_pao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE proiect_pao;
SHOW DATABASES;
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
/*  MEDICS – one-to-one with persons                                          */
/* -------------------------------------------------------------------------- */
CREATE TABLE medics (
    id                   INT PRIMARY KEY,           -- FK = persons.id
    specialization       VARCHAR(100),
    years_experience     INT,
    CONSTRAINT fk_medics_person
        FOREIGN KEY (id) REFERENCES persons(id) ON DELETE CASCADE
);

/* -------------------------------------------------------------------------- */
/*  PATIENTS – one-to-one with persons                                        */
/* -------------------------------------------------------------------------- */
CREATE TABLE patients (
    id           INT PRIMARY KEY,                   -- FK = persons.id
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
    type      VARCHAR(50)  -- eg. VIRAL, BACTERIAL …
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
    patient_id        INT,                  -- nullable if you ever need it
    date_time         DATETIME NOT NULL,
    duration_minutes  INT,
    type              VARCHAR(50),          -- enum stored as string
    disease_id        INT,
    cost              DOUBLE,
    clinique_id       INT,
    room_number       INT,
    CONSTRAINT fk_appt_medic     FOREIGN KEY (medic_id)   REFERENCES medics(id),
    CONSTRAINT fk_appt_patient   FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_appt_disease   FOREIGN KEY (disease_id) REFERENCES diseases(id),
    CONSTRAINT fk_appt_clinique  FOREIGN KEY (clinique_id)REFERENCES cliniques(id)
);

/* -------------------------------------------------------------------------- */
/*  SAMPLE DATA                                                               */
/* -------------------------------------------------------------------------- */

-- Persons
INSERT INTO persons (first_name,last_name,age,birth,phone_number,email,address) VALUES
  ('John','Doe', 45,'1980-05-01','0712345678','john.doe@med.com','Str. Sanatatii 1'),
  ('Anna','Smith',32,'1993-08-15','0722334455','anna.smith@mail.com','Str. Florilor 10');

-- Medics (John Doe)
INSERT INTO medics (id,specialization,years_experience) VALUES
  (1,'CARDIOLOGY',20);

-- Patients (Anna Smith)
INSERT INTO patients (id,insured) VALUES
  (2,TRUE);

-- Cliniques
INSERT INTO cliniques (name,address) VALUES
  ('Central Clinique','Bucharest, Bd. Independentei 99');

-- Rooms
INSERT INTO rooms (clinique_id,room_type,floor,room_number) VALUES
  (1,'CONSULTATION',1,101),
  (1,'OPERATING',2,202);

-- Diseases
INSERT INTO diseases (name,type) VALUES
  ('Hypertension','CHRONIC'),
  ('Influenza','VIRAL');

-- Med File
INSERT INTO med_files (patient_id,registration_date) VALUES
  (2,'2024-01-10');

-- Appointment
INSERT INTO appointments (medic_id,patient_id,date_time,duration_minutes,type,disease_id,cost,clinique_id,room_number)
VALUES
  (1,2,'2025-06-06 10:30:00',30,'CHECKUP',1,250.00,1,101);


CREATE TABLE calendars (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  start_hour       INT        NOT NULL,
  end_hour         INT        NOT NULL,
  free_days        VARCHAR(255),
  appointed_dates  TEXT
);

ALTER TABLE medics
  ADD COLUMN calendar_id INT UNIQUE,
  ADD CONSTRAINT fk_medics_calendar
    FOREIGN KEY (calendar_id) REFERENCES calendars(id) ON DELETE SET NULL;
    

INSERT INTO calendars
  (start_hour, end_hour, free_days, appointed_dates)
VALUES
  (8,
   17,
   '1,15,42,100',
   '2025-06-06T10:30:00#30;2025-06-07T11:00:00#45'
);

-- 2) grab its auto‐generated ID and assign it to medic #42
SET @calID = LAST_INSERT_ID();

UPDATE medics
   SET calendar_id = @calID
 WHERE id = 42;
 