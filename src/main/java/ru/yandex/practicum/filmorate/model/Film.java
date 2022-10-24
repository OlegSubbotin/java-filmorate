package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.MinReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200, message = "Description more 200 characters")
    private String description;
    @MinReleaseDate
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    Set<Long> likes = new HashSet<>();

    public Integer getRate() {
        return likes.size();
    }
}
