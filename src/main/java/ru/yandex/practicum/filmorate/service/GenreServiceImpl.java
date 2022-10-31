package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDbStorage genreDbStorage;

    public GenreServiceImpl(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Genre> getAll() {
        return genreDbStorage.getAll();
    }

    @Override
    public Genre findGenreById(Long id) {
        return genreDbStorage.findGenreById(id).orElseThrow(()-> new NotFoundException("Genre not found"));
    }
}
