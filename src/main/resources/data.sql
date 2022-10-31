MERGE INTO genre (ID, NAME)
    VALUES
        (1, 'Комедия'),
        (2, 'Драма'),
        (3, 'Мультфильм'),
        (4, 'Фантастика'),
        (5, 'Приключения'),
        (6, 'Детектив');
MERGE INTO mpa (ID, MPA_NAME, MEANING)
VALUES
    (1, 'G', 'General audiences'),
    (2, 'PG', 'Parental guidance suggested'),
    (3, 'PG-13', 'Parents strongly cautioned'),
    (4, 'R', 'Restricted'),
    (5, 'NC-17', 'Clearly adult');
-- ALTER TABLE mpa ALTER COLUMN id RESTART WITH 6;