package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.enums.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getAll(@RequestParam(required = false) String text,
                                 @RequestParam(required = false) List<Long> categories,
                                 @RequestParam(required = false) Boolean paid,
                                 @RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime rangeStart,
                                 @RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime rangeEnd,
                                 @RequestParam(required = false) Boolean onlyAvailable,
                                 @RequestParam(required = false) EventSort sort,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                 HttpServletRequest request) {
        log.info("\n\nполучение всех событий публичное\n");
        return eventService
                .getAllByUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventDto get(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("\n\nполучение события публичное\n");
        return eventService.getByUser(eventId, request);
    }

}
