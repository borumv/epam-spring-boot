-- Очистка таблиц перед вставкой данных
TRUNCATE TABLE trainer_training_type CASCADE;
TRUNCATE TABLE trainee_trainer CASCADE;
TRUNCATE TABLE training CASCADE;
TRUNCATE TABLE trainees CASCADE;
TRUNCATE TABLE trainers CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE training_types CASCADE;

-- Вставка типов тренировок (вставляем их в первую очередь, чтобы они были доступны для связей)
INSERT INTO training_types (id, name)
VALUES
    (1, 'CARDIO'),
    (2, 'STRENGTH_TRAINING');

-- Вставка пользователей
INSERT INTO users (first_name, last_name, username, password, is_active, role, failed_attempt, account_non_locked)
VALUES
    ('John', 'Doe', 'john.doe', '$2a$10$WyCYPLQRJTiV8g1lsnuvpeJWHfdn7PWeV.70omdjT0ktIKgtkT7j.', TRUE, 'USER', 0, true),
    ('Jane', 'Smith', 'jane.smith', '$2a$10$WyCYPLQRJTiV8g1lsnuvpeJWHfdn7PWeV.70omdjT0ktIKgtkT7j.', TRUE, 'USER', 0, true),
    ('Alice', 'Johnson', 'alice.johnson', '$2a$10$WyCYPLQRJTiV8g1lsnuvpeJWHfdn7PWeV.70omdjT0ktIKgtkT7j.', TRUE, 'USER', 0, true),
    ('Bob', 'Brown', 'bob.brown', '$2a$10$WyCYPLQRJTiV8g1lsnuvpeJWHfdn7PWeV.70omdjT0ktIKgtkT7j.', TRUE, 'USER', 0, true);

-- Вставка тренируемых
INSERT INTO trainees (id, date_of_birth, address)
VALUES
    ((SELECT id FROM users WHERE username='john.doe'), '1990-01-01', '123 Main St'),
    ((SELECT id FROM users WHERE username='jane.smith'), '1992-02-01', '456 Elm St');

-- Вставка тренеров
INSERT INTO trainers (id)
VALUES
    ((SELECT id FROM users WHERE username='alice.johnson')),
    ((SELECT id FROM users WHERE username='bob.brown'));

-- Вставка тренировок
INSERT INTO training (name, date, duration, trainee_id, trainer_id, training_type_id)
VALUES
    ('Morning Run', '2023-01-01', 30, (SELECT id FROM trainees WHERE id=(SELECT id FROM users WHERE username='john.doe')), (SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='alice.johnson')), 1),
    ('Evening Lift', '2023-01-01', 45, (SELECT id FROM trainees WHERE id=(SELECT id FROM users WHERE username='jane.smith')), (SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='bob.brown')), 2);

-- Вставка связей между тренерами и типами тренировок
INSERT INTO trainer_training_type (trainer_id, training_type_id)
VALUES
    ((SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='alice.johnson')), 1),
    ((SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='alice.johnson')), 2),
    ((SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='bob.brown')), 1);

-- Вставка связей между тренируемыми и тренерами
INSERT INTO trainee_trainer (trainee_id, trainer_id)
VALUES
    ((SELECT id FROM trainees WHERE id=(SELECT id FROM users WHERE username='john.doe')), (SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='alice.johnson'))),
    ((SELECT id FROM trainees WHERE id=(SELECT id FROM users WHERE username='jane.smith')), (SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='bob.brown'))),
    ((SELECT id FROM trainees WHERE id=(SELECT id FROM users WHERE username='john.doe')), (SELECT id FROM trainers WHERE id=(SELECT id FROM users WHERE username='bob.brown')));
