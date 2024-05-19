package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
