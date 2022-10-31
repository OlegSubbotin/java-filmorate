package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Film save(Film film) {
        if (film.getId() == null) {
            film.setId(nextId++);
        } else if (film.getId() > nextId) {
            throw new ValidationException("Invalid Id");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        return null;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        return null;
    }

    @Override
    public List<Film> getMostPopularFilms() {
        return null;
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        if (films.containsKey(id)) {
            return Optional.of(films.get(id));
        } else {
            return Optional.empty();
        }
    }
}
