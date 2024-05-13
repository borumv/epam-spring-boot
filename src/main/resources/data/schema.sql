DROP TABLE IF EXISTS training CASCADE;
DROP TABLE IF EXISTS trainers CASCADE;
DROP TABLE IF EXISTS trainees CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS training_types CASCADE;
DROP TABLE IF EXISTS trainee_trainer CASCADE;
DROP TABLE IF EXISTS trainer_training_type CASCADE;

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       username VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       is_active BOOLEAN
);

CREATE TABLE trainees (
                          id BIGINT PRIMARY KEY,
                          date_of_birth DATE,
                          address VARCHAR(255),
                          FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE trainers (
                          id BIGINT PRIMARY KEY,
                          FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE training_types (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(255)
);

CREATE TABLE training (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255),
                          date DATE,
                          duration INT,
                          trainee_id BIGINT,
                          trainer_id BIGINT,
                          training_type_id INT,
                          FOREIGN KEY (trainee_id) REFERENCES trainees(id),
                          FOREIGN KEY (trainer_id) REFERENCES trainers(id),
                          FOREIGN KEY (training_type_id) REFERENCES training_types(id)
);

CREATE TABLE trainee_trainer (
                                 trainee_id BIGINT,
                                 trainer_id BIGINT,
                                 PRIMARY KEY (trainee_id, trainer_id),
                                 FOREIGN KEY (trainee_id) REFERENCES trainees(id),
                                 FOREIGN KEY (trainer_id) REFERENCES trainers(id)
);

CREATE TABLE trainer_training_type (
                                       trainer_id BIGINT,
                                       training_type_id INT,
                                       PRIMARY KEY (trainer_id, training_type_id),
                                       FOREIGN KEY (trainer_id) REFERENCES trainers(id),
                                       FOREIGN KEY (training_type_id) REFERENCES training_types(id)
);
