package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film save(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("releaseDate", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        if (film.getMpa() != null) {
            parameters.put("mpa_id", film.getMpa().getId());
        } else {
            throw new ValidationException("mpa is null");
        }
        Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(id.longValue());
        updateGenre(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILM SET NAME=?, DESCRIPTION=?, RELEASEDATE=?, DURATION=?, MPA_ID=? WHERE ID=?";
        try {
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());
        } catch (DataIntegrityViolationException ex) {
            throw new NotFoundException("Film not found");
        }
        updateGenre(film);
        return findFilmById(film.getId()).orElseThrow(() -> new NotFoundException("Film not found"));
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM film LEFT JOIN mpa AS m ON film.mpa_id=m.id";
        return jdbcTemplate.query(sql, this::mapRowFilm);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, filmId, userId);
        } catch (DataIntegrityViolationException ex) {
            throw new NotFoundException("Film not found or like already exist");
        }
        return findFilmById(filmId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        String sql = "DELETE FROM LIKES WHERE FILM_ID=? AND USER_ID=?";
        try {
            jdbcTemplate.update(sql, filmId, userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Film not found");
        }
        return findFilmById(filmId).orElseThrow(() -> new NotFoundException("Film not found"));
    }

    @Override
    public List<Film> getMostPopularFilms() {
        String sql = "SELECT FILM_ID, COUNT(USER_ID) as count FROM LIKES" +
                " GROUP BY FILM_ID ORDER BY count DESC LIMIT 100";
        List<Long> ids = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id"));
        return findFilmsByListId(ids);
    }

    private List<Film> findFilmsByListId(List<Long> ids) {
        String sql = "select f.*, M.mpa_name\n" +
                "FROM FILM as f\n" +
                "LEFT JOIN MPA M on M.ID = f.MPA_ID\n" +
                "WHERE f.ID IN (?)";
        return jdbcTemplate.query(sql, this::mapRowFilm, ids);
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sql = "select *\n" +
                "FROM FILM as f\n" +
                "LEFT JOIN MPA M on M.ID = f.MPA_ID\n" +
                "WHERE f.ID=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowFilm, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    private Set<Genre> getGenres(Long filmId) {
        String sql = "SELECT g.* FROM FILM_GENRE\n" +
                "LEFT JOIN GENRE G on G.ID = FILM_GENRE.GENRE_ID\n" +
                "where FILM_ID=? ORDER BY genre_id";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, num) -> Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build(), filmId));
    }

    private Film mapRowFilm(ResultSet rs, int nowRow) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder().id(rs.getLong("mpa_id")).name(rs.getString("mpa_name")).build())
                .genres(getGenres(rs.getLong("id")))
                .build();
        film.setLikes(getLikesByFilmId(film.getId()));
        return film;
    }

    private Set<Long> getLikesByFilmId(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id=?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, num) -> rs.getLong("user_id"), filmId));
    }

    private void updateGenre(Film film) {
        String deleteSql = "DELETE FROM film_genre WHERE film_id=?";
        jdbcTemplate.update(deleteSql, film.getId());
        String sql = "INSERT INTO film_genre(film_id, genre_id) VALUES(?, ?)";
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> {
                jdbcTemplate.update(sql, film.getId(), genre.getId());
            });
        }
    }
}
