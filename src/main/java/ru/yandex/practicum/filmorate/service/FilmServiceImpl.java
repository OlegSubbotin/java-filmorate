package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                           @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film create(Film film) {
        return filmStorage.save(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id).orElseThrow(() -> new NotFoundException("Film not found"));
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        checkUserId(userId);
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        checkUserId(userId);
        return filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getMostPopularFilms(Long count) {
        List<Film> films = filmStorage.getAll();
        if (films.isEmpty()) {
            throw new ValidationException("List of films is empty");
        }
        return films.stream().sorted(Comparator.comparing(Film::getRate).reversed()).limit(count).collect(Collectors.toList());
    }

    private void checkUserId(Long userId) {
        userStorage.findUserById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }
}