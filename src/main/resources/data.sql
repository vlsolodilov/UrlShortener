INSERT INTO USERS (SECRET_KEY)
VALUES ('key1'),
       ('key2');

INSERT INTO LINKS (ORIGINAL_URL, SHORT_URL, CREATED_TO, USER_ID)
VALUES ('https://www.google.com', 'aaa', null, 1),
       ('https://yandex.ru', 'bbb', null, 1),
       ('https://www.google.com', 'ccc', null, 2),
       ('https://yandex.ru', 'ddd', null, 2),
       ('https://yandex.ru', 'eee', '2021-02-13 22:00:00', 2),
       ('https://yandex.ru', 'fff', '2023-02-13 22:00:00', 2);

INSERT INTO REDIRECTS (TIME_REDIRECT, URL, USER_ID)
VALUES ('2022-02-13 22:00:00', 'https://www.google.com', 1),
       ('2022-02-13 22:01:00', 'https://yandex.ru', 1),
       ('2022-02-13 22:02:00', 'https://www.google.com', 1),
       ('2022-02-13 22:03:00', 'https://yandex.ru', 2),
       ('2022-02-13 22:04:00', 'https://yandex.ru', 1);