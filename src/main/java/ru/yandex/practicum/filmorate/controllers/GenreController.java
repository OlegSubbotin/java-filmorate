package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreServiceImpl genreServiceImpl;

    @GetMapping
    public List<Genre> getAll() {
        return genreServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Genre findGenreById(@Valid @PathVariable Long id) {
        log.info("Genre id: {}", id);
        return genreServiceImpl.findGenreById(id);
    }
}
