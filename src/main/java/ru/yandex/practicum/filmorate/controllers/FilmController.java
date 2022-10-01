package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();

    private int nextId = 1;

    @PostMapping
    public Film createFilm(@RequestBody Film film, HttpServletRequest request) {
        if (filmIsValid(film)) {
            film.setId(getNextId());
            films.put(film.getId(), film);
        }
        log.info("Получен запрос к эндпоинту: '{} {}', Добавлен фильм: '{}'",
                request.getMethod(), request.getRequestURI(), film);
        return film;
    }

    @PutMapping
    public Film changedFilm(@RequestBody Film film) {
        if (filmIsValid(film)) {
            if (films.containsKey(film.getId())) {
                films.remove(film.getId());
                films.put(film.getId(), film);
                return film;
            } else {
                throw new NotFoundException("Id not found");
            }
        }
        return film;
    }

    @GetMapping
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private boolean filmIsValid(Film film) {
        if (film.getName().isEmpty() || film.getName().equals("")) {
            throw new ValidationException("Film name is empty");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Description more 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("ReleaseDate is before 28.12.1985");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Film duration cannot be negative");
        } else {
            return true;
        }
    }

    private int getNextId(){
        return nextId++;
    }
}


