CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       username VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       is_active BOOLEAN
);

CREATE TABLE trainees (
                          id INT PRIMARY KEY REFERENCES users(id),
                          date_of_birth DATE,
                          address VARCHAR(255)
);

CREATE TABLE trainers (
                          id INT PRIMARY KEY REFERENCES users(id),
                          specialization VARCHAR(255)
);

CREATE TABLE training_types (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(255) UNIQUE
);

CREATE TABLE trainings (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255),
                           date DATE,
                           duration INT,
                           trainee_id INTEGER REFERENCES trainees(id),
                           trainer_id INTEGER REFERENCES trainers(id),
                           training_type_id INTEGER REFERENCES training_types(id)
);

CREATE TABLE trainee_trainer (
                                 trainee_id INTEGER REFERENCES trainees(id),
                                 trainer_id INTEGER REFERENCES trainers(id),
                                 PRIMARY KEY (trainee_id, trainer_id)
);

CREATE TABLE trainer_specializations (
                                         trainer_id INTEGER REFERENCES trainers(id),
                                         training_type_id INTEGER REFERENCES training_types(id),
                                         PRIMARY KEY (trainer_id, training_type_id)
);

CREATE TABLE trainee_trainer (
                                 trainee_id INTEGER REFERENCES trainees(id),
                                 trainer_id INTEGER REFERENCES trainers(id),
                                 PRIMARY KEY (trainee_id, trainer_id)
);