-- Вставка пользователей
INSERT INTO users (first_name, last_name, username, password, is_active) VALUES
                                                                             ('John', 'Doe', 'john.doe', 'pass123', TRUE),
                                                                             ('Jane', 'Smith', 'jane.smith', 'pass123', TRUE),
                                                                             ('Alice', 'Johnson', 'alice.johnson', 'pass123', TRUE),  -- Добавлен тренер
                                                                             ('Bob', 'Brown', 'bob.brown', 'pass123', TRUE);          -- Добавлен тренер

-- Вставка тренировок (тренируемые)
INSERT INTO trainees (id, date_of_birth, address) VALUES
                                                      ((SELECT id FROM users WHERE username='john.doe'), '1990-01-01', '123 Main St'),
                                                      ((SELECT id FROM users WHERE username='jane.smith'), '1992-02-01', '456 Elm St');

-- Вставка тренеров
INSERT INTO trainers (id) VALUES
                              ((SELECT id FROM users WHERE username='alice.johnson')),
                              ((SELECT id FROM users WHERE username='bob.brown'));

-- Вставка типов тренировок
INSERT INTO training_types (name) VALUES
                                      ('CARDIO'),
                                      ('STRENGTH_TRAINING');


INSERT INTO training (name, date, duration, trainee_id, trainer_id, training_type_id) VALUES
                                                                                          ('Morning Run', '2023-01-01', 30, (SELECT id FROM trainees WHERE id=(SELECT id FROM users WHERE username='john.doe')), (SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='alice.johnson')), 1),
                                                                                          ('Evening Lift', '2023-01-01', 45, (SELECT id FROM trainees WHERE id=(SELECT id FROM users WHERE username='jane.smith')), (SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='bob.brown')), 2);
INSERT INTO trainer_training_type VALUES
    (3,1),
    (3,2),
    (4,1);

INSERT INTO trainee_trainer VALUES
                                (1,3),
                                (2,4),
                                (1,4)

