package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventUpdateAdminDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.utils.Pattern;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventDto> getAll(@RequestParam(required = false) Long users,
                                 @RequestParam(required = false) EventState states,
                                 @RequestParam(required = false) Long categories,
                                 @RequestParam(required = false)
                                     @DateTimeFormat(pattern = Pattern.DATETIME) LocalDateTime rangeStart,
                                 @RequestParam(required = false)
                                     @DateTimeFormat(pattern = Pattern.DATETIME) LocalDateTime rangeEnd,
                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("\n\nполучение событий админом\n");
        return eventService
                .getAllByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventDto update(@RequestBody @Valid EventUpdateAdminDto eventUpdateAdminDto,
                           @PathVariable Long eventId) {
        log.info("\n\nобновление события админом\n");
        return eventService.updateByAdmin(eventUpdateAdminDto, eventId);
    }



}
