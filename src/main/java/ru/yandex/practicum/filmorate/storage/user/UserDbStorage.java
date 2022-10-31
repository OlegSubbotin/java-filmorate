package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConflictException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("birthday", user.getBirthday());
        try {
            Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
            user.setId(id.longValue());
            return user;
        } catch (DuplicateKeyException ex) {
            throw new ConflictException("Email already in use");
        }
    }

    public User update(User user) {
        String sql = "UPDATE users SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? WHERE ID=?";
        try {
            jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                    user.getName(), user.getBirthday(), user.getId());
        } catch (DataIntegrityViolationException ex) {
            throw new NotFoundException("User not found");
        }
        return findUserById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowUser);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowUser, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public User addFriend (Long userId, Long friendId) {
        String sql = "INSERT INTO friends (USER_ID, FRIENDS_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (DataIntegrityViolationException ex) {
            throw new NotFoundException("User not found or friendship already exist");
        }
        return findUserById(userId).orElseThrow(()-> new NotFoundException("User not found"));
    }

    public User deleteFriend (Long userId, Long friendId) {
        String sql = "DELETE FROM FRIENDS WHERE FRIENDS_ID=? AND USER_ID=?";
        try {
            jdbcTemplate.update(sql, friendId, userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("User not found");
        }
        return findUserById(userId).orElseThrow(()-> new NotFoundException("User not found"));
    }

    private User mapRowUser(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(getFriendsIdByUserId(rs.getLong("id")))
                .build();
        user.setFriends(getFriendsIdByUserId(user.getId()));
        return user;
    }

    private Set<Long> getFriendsIdByUserId (Long id) {
        String sql = "SELECT friends_id FROM friends WHERE user_id=?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, num) -> rs.getLong("friends_id"), id));
    }
}
