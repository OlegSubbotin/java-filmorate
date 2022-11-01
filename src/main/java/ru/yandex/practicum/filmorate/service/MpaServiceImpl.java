package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {
    private final MpaDbStorage mpaDbStorage;

    public MpaServiceImpl(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }


    @Override
    public List<Mpa> getAll() {
        return mpaDbStorage.getAll();
    }

    @Override
    public Mpa findGenreById(Long id) {
        return mpaDbStorage.findMpaById(id).orElseThrow(()-> new NotFoundException("Genre not found"));
    }
}
