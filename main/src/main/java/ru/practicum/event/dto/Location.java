package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @NotNull
    @Min(-90)
    @Max(90)
    private Float lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private Float lon;
}
