package ru.practicum.comment.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
@ToString
public class CommentNewDto {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 2000)
    private String content;
    @NotNull
    private Long event;
}
