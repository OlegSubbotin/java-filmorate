package ru.yandex.practicum.filmorate.storage.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Repository
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO friends (USER_ID, FRIENDS_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (DataIntegrityViolationException ex) {
            throw new NotFoundException("User not found or friendship already exist");
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friends WHERE FRIENDS_ID=? AND USER_ID=?";
        try {
            jdbcTemplate.update(sql, friendId, userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("User not found");
        }
    }

}
