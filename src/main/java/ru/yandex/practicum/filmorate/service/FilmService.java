package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film findFilmById(Integer id);

    Film addLike(Integer id, Integer userId);

    Film deleteLike(Integer id, Integer userId);

    List<Film> getMostPopularFilms(Integer count);
}
