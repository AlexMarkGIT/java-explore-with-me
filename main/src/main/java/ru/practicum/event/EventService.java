package ru.practicum.event;

import ru.practicum.event.dto.*;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.enums.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto create(EventNewDto eventNewDto, Long userId);

    EventDto updateEventByInitiator(EventUpdateUserDto eventUpdateUserDto,
                                    Long userId,
                                    Long eventId);

    EventDto updateByAdmin(EventUpdateAdminDto eventUpdateAdminDto, Long eventId);

    List<EventShortDto>  getAllByInitiator(Long userId, Integer from, Integer size);

    List<EventDto> getAllByAdmin(List<Long> users,
                                 EventState states,
                                 List<Long> categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 Integer from,
                                 Integer size);

    List<EventDto> getAllByUser(String text,
                                List<Long> categories,
                                Boolean paid,
                                LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                Boolean onlyAvailable,
                                EventSort sort,
                                Integer from,
                                Integer size,
                                HttpServletRequest request);

    EventDto getByInitiator(Long userId, Long eventId);

    EventDto getByUser(Long eventId, HttpServletRequest request);
}
