package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User update(User user);

    List<User> getAll();

    User findUserById(Integer id);

    User addFriend(Integer id, Integer friendId);

    User deleteFriend(Integer id, Integer friendId);

    List<User> getFriends(Integer id);

    List<User> getMutualFriends(Integer id, Integer friendId);
}
