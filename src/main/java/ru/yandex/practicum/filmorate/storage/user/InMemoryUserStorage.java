package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
        } else if (user.getId() > nextId) {
            throw new ValidationException("Invalid Id");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }
}
