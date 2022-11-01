package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private final FriendsStorage friendsStorage;

    @Autowired
    public UserServiceImpl(@Qualifier("userDbStorage") UserStorage userStorage,
                           FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    @Override
    public User save(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.save(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User findUserById(Long id) {
        return userStorage.findUserById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User addFriend(Long id, Long friendId) {
        friendsStorage.addFriend(id, friendId);
        return findUserById(id);
    }

    @Override
    public User deleteFriend(Long id, Long friendId) {
       friendsStorage.deleteFriend(id, friendId);
        return findUserById(id);
    }

    @Override
    public List<User> getFriends(Long id) {
        User user = findUserById(id);
        return user.getFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriends(Long id, Long friendId) {
        User user = findUserById(id);
        User friend = findUserById(friendId);
        Set<Long> mutualFriends = new HashSet<>(user.getFriends());
        mutualFriends.retainAll(friend.getFriends());
        return mutualFriends.stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }
}
