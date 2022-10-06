package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film create(Film film) {
        return filmStorage.save(film);
    }

    @Override
    public Film update(Film film) {
        findFilmById(film.getId());
        return filmStorage.save(film);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id).orElseThrow(() -> new NotFoundException("Film not found"));
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        Film film = findFilmById(id);
        film.getLikes().add(userId);
        return filmStorage.save(film);
    }

    @Override
    public Film deleteLike(Integer id, Integer userId) {
        Film film = findFilmById(id);
        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
            return filmStorage.save(film);
        } else {
            throw new NotFoundException("User don`t liked this film");
        }
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> films = filmStorage.getAll();
        if (films.isEmpty()) {
            throw new ValidationException("List of films is empty");
        }
        return films.stream().sorted(Comparator.comparing(Film::getRate).reversed()).limit(count).collect(Collectors.toList());
    }
}