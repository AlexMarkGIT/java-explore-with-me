package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@RequestBody @Valid EventNewDto eventNewDto,
                           @PathVariable Long userId) {
        log.info("\n\nсоздание события\n");
        return eventService.create(eventNewDto, userId);
    }

    @PatchMapping("/{eventId}")
    public EventDto update(@RequestBody @Valid EventUpdateUserDto eventUpdateUserDto,
                                      @PathVariable Long userId,
                                      @PathVariable Long eventId) {
        log.info("\n\nобеовление события иницатором\n");
        return eventService.updateEventByInitiator(eventUpdateUserDto, userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getAll(@PathVariable Long userId,
                                      @RequestParam(name = "from",
                                              defaultValue = "0",
                                              required = false) Integer from,
                                      @RequestParam(name = "size",
                                              defaultValue = "10",
                                              required = false) Integer size) {
        log.info("\n\nполучение всех событий инициатором\n");
        return eventService.getAllByInitiator(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDto get(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("\n\nполучение события инициатором\n");
        return eventService.getByInitiator(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByInitiator(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        log.info("\n\nполучение всех запросов на участие инициатором события\n");
        return requestService.getRequestsByInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResponse updateRequests(@PathVariable Long userId,
                                                           @PathVariable Long eventId,
                                                           @RequestBody EventRequestStatusUpdateRequest reqsUpdReq) {
        log.info("\n\nобновление запросов на участие инициатором события\n");
        return requestService.updateRequestsByInitiator(userId, eventId, reqsUpdReq);
    }

}
