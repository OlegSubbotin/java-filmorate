package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (userIsValid(user)) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(getNextId());
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User changedUser(@RequestBody User user) {
        if (userIsValid(user)) {
            if (users.containsKey(user.getId())) {
                users.remove(user.getId());
                users.put(user.getId(), user);
            } else {
                throw new NotFoundException("Id not found");
            }
        }
        return user;
    }

    @GetMapping
    public ArrayList<User> getUsers() {
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

    private int getNextId(){
        return nextId++;
    }

}
