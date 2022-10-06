package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer nextId = 1;

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
    public Optional<User> findUserById(Integer id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        } else {
            return Optional.empty();
        }
    }
}
