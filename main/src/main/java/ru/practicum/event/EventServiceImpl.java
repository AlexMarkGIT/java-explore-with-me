package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.AdminStateAction;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.UserStateAction;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.utils.Pattern;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    public EventDto create(EventNewDto eventNewDto, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        Category category = categoryRepository.findById(eventNewDto.getCategory())
                .orElseThrow(() -> new NotFoundException("такой категории не существует"));

        Event event = eventMapper.toNewEntity(eventNewDto);

        event.setInitiator(user);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0L);
        event.setViews(0L);

        if (eventNewDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (eventNewDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (eventNewDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventDto updateEventByInitiator(EventUpdateUserDto dto,
                                           Long userId,
                                           Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        Event event = eventRepository.findEventByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));

        if (dto.getCategory() != null && !dto.getCategory().equals(event.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("такая категория не найдена"));

            event.setCategory(newCategory);
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("нельзя изменять уже опубликованное событие");
        }

        if (dto.getStateAction() != null) {

            if (dto.getStateAction().equals(UserStateAction.CANCEL_REVIEW)
                    && event.getState().equals(EventState.PENDING)) {
                event.setState(EventState.CANCELED);
            } else if (dto.getStateAction().equals(UserStateAction.SEND_TO_REVIEW)
                    && event.getState().equals(EventState.CANCELED)) {
                event.setState(EventState.PENDING);
            }

        }

        eventMapper.updateEventFromDtoByUser(dto, event);

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventDto updateByAdmin(EventUpdateAdminDto dto, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("нельзя изменять опубликованное или отмененное событие");
        }

        if (dto.getCategory() != null && !dto.getCategory().equals(event.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("такая категория не найдена"));

            event.setCategory(newCategory);
        }

        if (dto.getStateAction() != null) {
            if (dto.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else {
                event.setState(EventState.CANCELED);
            }
        }

        eventMapper.updateEventFromDtoByAdmin(dto, event);

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventDto getByInitiator(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        Event event = eventRepository.findEventByIdAndInitiator(eventId, user)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));

        return eventMapper.toDto(event);
    }

    @Override
    public List<EventShortDto> getAllByInitiator(Long userId, Integer from, Integer size) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        List<Event> events = eventRepository
                .findAllByInitiator(user, PageRequest.of(from / size, size));

        return eventMapper.toShortDtoList(events);
    }

    @Override
    public List<EventDto> getAllByAdmin(Long users,
                                        EventState states,
                                        Long categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Integer from,
                                        Integer size) {

        String state = null;

        if (states != null) state = states.toString();

        List<Event> events = eventRepository
                .getAllEventsByParamsByAdmin(users,
                        state,
                        categories,
                        rangeStart,
                        rangeEnd,
                        PageRequest.of(from / size, size, Sort.by("id").ascending()));

        return eventMapper.toDtoList(events);
    }

    @Override
    public List<EventDto> getAllByUser(String text,
                                       Long categories,
                                       Boolean paid,
                                       LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd,
                                       Boolean onlyAvailable,
                                       EventSort sort,
                                       Integer from,
                                       Integer size,
                                       HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Ошибка запроса: rangeStart позже rangeEnd");
        }

        String sortString;

        if (sort == EventSort.EVENT_DATE) {
            sortString = "eventDate";
        } else {
            sortString = "id";
        }

        List<Event> events = eventRepository
                .getAllEventsByParams(text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        onlyAvailable,
                        PageRequest.of(from / size, size, Sort.by(sortString).descending()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Pattern.DATETIME);

        for (Event event : events) {
            String startTime = event.getCreatedOn().format(formatter);
            String endTime = LocalDateTime.now().format(formatter);
            List<String> uris = List.of("/events/" + event.getId());

            List<StatsDto> stats = statsClient.get(startTime, endTime, uris, false);

            if (stats.size() == 0) {
                event.setViews(1L);
            } else {
                event.setViews(stats.get(0).getHits() + 1);
            }
        }

        sendStatsForEventList(events, request);

        return eventMapper.toDtoList(events);
    }

    @Override
    public EventDto getByUser(Long eventId, HttpServletRequest request) {

        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Pattern.DATETIME);

        String startTime = event.getCreatedOn().format(formatter);
        String endTime = LocalDateTime.now().format(formatter);

        List<String> uris = List.of("/events/" + event.getId());

        List<StatsDto> stats = statsClient.get(startTime, endTime, uris, false);

        if (stats.size() == 0) {
            event.setViews(1L);
        } else {
            event.setViews(stats.get(0).getHits() + 1);
        }

        sendStatsForOneEvent(event, request);

        eventRepository.save(event);

        return eventMapper.toDto(event);
    }

    private void sendStatsForOneEvent(Event event, HttpServletRequest request) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Pattern.DATETIME);

        HitDto hitDto = HitDto.builder()
                .app("main-service")
                .uri("/events")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();

        statsClient.post(hitDto);

        hitDto.setUri("/events/" + event.getId());

        statsClient.post(hitDto);
   }

    private void sendStatsForEventList(List<Event> events, HttpServletRequest request) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Pattern.DATETIME);

        HitDto hitDto = HitDto.builder()
                .app("main-service")
                .uri("/events")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();

        statsClient.post(hitDto);

        for (Event event : events) {
            hitDto.setUri("/events/" + event.getId());

            statsClient.post(hitDto);
        }
    }



}
