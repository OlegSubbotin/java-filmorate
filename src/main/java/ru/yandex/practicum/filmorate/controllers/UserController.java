package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("User: {}", user);
        return userService.save(user);
    }

    @PutMapping
    public User changedUser(@Valid @RequestBody User user) {
        log.info("User: {}", user);
        return userService.update(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable @NotNull Long id) {
        log.info("User id: {}", id);
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable @NotNull Long id, @PathVariable @NotNull Long friendId) {
        log.info("User id: {}, friend id: {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable @NotNull Long id, @PathVariable @NotNull Long friendId) {
        log.info("User id: {}, friend id: {}", id, friendId);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @NotNull Long id) {
        log.info("User id: {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable @NotNull Long id, @PathVariable @NotNull Long otherId) {
        log.info("User id: {}, other id: {}", id, otherId);
        return userService.getMutualFriends(id, otherId);
    }
}
