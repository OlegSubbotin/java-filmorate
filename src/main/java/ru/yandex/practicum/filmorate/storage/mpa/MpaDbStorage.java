package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, num) -> Mpa.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("mpa_name"))
                .build());
    }

    @Override
    public Optional<Mpa> findMpaById(Long id) {
        String sql = "SELECT * FROM mpa WHERE id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, num) -> Mpa.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("mpa_name"))
                    .build(), id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

}
