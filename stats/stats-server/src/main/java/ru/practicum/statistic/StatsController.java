package ru.practicum.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.statistic.exception.TimePeriodException;
import ru.practicum.statistic.utils.Pattern;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveHit(@RequestBody @Valid HitDto hitDto) {
        log.info("Получен запрос добавления статистики");
        statsService.saveHit(hitDto);
    }

    @GetMapping("/stats")
    List<StatsDto> getStats(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {

        log.info("Получен запрос получения статистики");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Pattern.DATETIME);

        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        if (startTime.isAfter(endTime)) {
            throw new TimePeriodException("время старта не может быть позже времени конца");
        }

        if (uris == null) {
            uris = Collections.emptyList();
        }

        return statsService.getStats(startTime, endTime, uris, unique);
    }
}
