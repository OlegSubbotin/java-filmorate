package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public class FilmDbStorage implements FilmStorage{
    @Override
    public Film save(Film film) {
        return null;
    }

    @Override
    public List<Film> getAll() {
        return null;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        return Optional.empty();
    }
}
