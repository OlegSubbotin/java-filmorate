package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user, HttpServletRequest request) {
        if (userIsValid(user)) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
                log.info("Получен запрос к эндпоинту: '{} {}', Добавлен пользователь: '{}', Имя установлено: '{}'",
                        request.getMethod(), request.getRequestURI(), user, user.getName());
            }
            user.setId(nextId);
            nextId++;
            users.put(user.getId(), user);
            log.info("Получен запрос к эндпоинту: '{} {}', Добавлен пользователь: '{}'",
                    request.getMethod(), request.getRequestURI(), user);
        }
        return user;
    }

    @PutMapping
    public User changedUser(@Valid @RequestBody User user, HttpServletRequest request) {
        if (userIsValid(user)) {
            if (users.containsKey(user.getId())) {
                users.remove(user.getId());
                users.put(user.getId(), user);
                log.info("Получен запрос к эндпоинту: '{} {}', Изменен пользователь: '{}'",
                        request.getMethod(), request.getRequestURI(), user);
            } else {
                throw new NotFoundException("Id not found");
            }
        }
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    boolean userIsValid(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().equals("") || !user.getEmail().contains("@")) {
            throw new ValidationException("Bad email");
        }
        if (user.getLogin().isEmpty() || user.getLogin().equals("") || user.getLogin().contains(" ")) {
            throw new ValidationException("Bad login");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Bad birthday");
        } else {
            return true;
        }
    }
}
