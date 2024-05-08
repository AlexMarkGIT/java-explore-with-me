package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Setter
public class HitDto {
    private Long id;
    @NotNull
    private String app;
    @NotNull
    private String uri;
    @NotNull
    @Size(min = 1, max = 15)
    private String ip;
    @NotNull
    private String timestamp;
}
