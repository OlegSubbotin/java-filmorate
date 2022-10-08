package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Film: {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film changedFilm(@Valid @RequestBody Film film) {
        log.info("Film: {}", film);
        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@Valid @PathVariable Integer id) {
        log.info("Film id: {}", id);
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Film id: {}, user id: {}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Film id: {}, user id: {}", id, userId);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Film count: {}", count);
        return filmService.getMostPopularFilms(count);
    }
}


