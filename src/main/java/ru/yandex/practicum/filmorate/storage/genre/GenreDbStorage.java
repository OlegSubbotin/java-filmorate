package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, (rs, num) -> Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build());
    }

    @Override
    public Optional<Genre> findGenreById(Long id) {
        String sql = "SELECT * FROM genre WHERE id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, num) -> Genre.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .build(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
