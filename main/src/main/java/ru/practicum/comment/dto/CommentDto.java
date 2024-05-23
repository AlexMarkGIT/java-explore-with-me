package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.utils.Pattern;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {
    private Long id;
    private String content;
    @JsonFormat(pattern = Pattern.DATETIME, shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;
    @JsonFormat(pattern = Pattern.DATETIME, shape = JsonFormat.Shape.STRING)
    private LocalDateTime published;
    private CommentStatus status;
    private Long commentator;
    private Long event;
}
