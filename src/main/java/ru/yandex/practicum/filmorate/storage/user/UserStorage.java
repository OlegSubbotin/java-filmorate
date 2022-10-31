package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User save(User user);

    List<User> getAll();

    Optional<User> findUserById(Long id);

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId);

    User update(User user);
}
