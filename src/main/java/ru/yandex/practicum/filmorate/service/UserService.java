package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User update(User user);

    List<User> getAll();

    User findUserById(Long id);

    User addFriend(Long id, Long friendId);

    User deleteFriend(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getMutualFriends(Long id, Long friendId);
}
