DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, user_id, description, calories)
VALUES ('2020-01-30 10:00:00', 100000, 'Завтрак', 500),
       ('2020-01-30 13:00:00', 100000, 'Обед', 1000),
       ('2020-01-30 20:00:00', 100000, 'Ужин', 500),
       ('2020-01-31 00:00:00', 100000, 'Еда на граничное значение', 100),
       ('2020-01-31 10:00:00', 100000, 'Завтрак', 1000),
       ('2020-01-31 13:00:00', 100000, 'Обед', 500),
       ('2020-01-31 20:00:00', 100000, 'Ужин', 410),
       ('2020-06-01 14:00:00', 100001, 'Админ ланч', 510),
       ('2020-06-01 21:00:00', 100001, 'Админ ужин', 1500);