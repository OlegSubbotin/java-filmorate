package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    List<Genre> getAll();

    Genre findGenreById(Long id);
}
