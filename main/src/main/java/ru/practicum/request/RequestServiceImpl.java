package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResponse;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.exception.AlreadyExistException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.request.enums.RequestStatus.CONFIRMED;
import static ru.practicum.request.enums.RequestStatus.REJECTED;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    public RequestDto create(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));

        if (requestRepository.existsByEventAndRequester(event, user)) {
            throw new AlreadyExistException("такой запрос уже существует");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("инициатор не может быть участником");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("событие не опубликовано или отменено");
        }

        if (event.getParticipantLimit() != 0 &&
                event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("превышен лимит участников");
        }

        Request request = new Request();

        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        Request newRequest = requestRepository.save(request);
        return requestMapper.toDto(newRequest);
    }

    @Override
    public RequestDto cancel(Long userId, Long requestId) {

        Request request = requestRepository.findByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new NotFoundException("такого запроса не существует"));

        if (request.getStatus().equals(CONFIRMED)) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }

        request.setStatus(RequestStatus.CANCELED);

        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getAll(Long userId) {

        User participant = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        List<Request> requests = requestRepository.findAllByRequesterId(participant.getId());

        return requestMapper.toDtoList(requests);
    }

    @Override
    public List<RequestDto> getRequestsByInitiator(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));

        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("пользователь не является инициатором события");
        }

        List<Request> requests = requestRepository.findAllByEvent(event);

        return requestMapper.toDtoList(requests);
    }

    @Override
    public EventRequestStatusUpdateResponse updateRequestsByInitiator(Long userId,
                                                                      Long eventId,
                                                                      EventRequestStatusUpdateRequest reqsUpdReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));

        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ConflictException("пользователь не является инициатором события");
        }

        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("лимит заявок исчерпан");
        }

        EventRequestStatusUpdateResponse updateResponse = new EventRequestStatusUpdateResponse();
        updateResponse.setConfirmedRequests(new ArrayList<>());
        updateResponse.setRejectedRequests(new ArrayList<>());

        List<Long> requestsIds = new ArrayList<>(reqsUpdReq.getRequestIds());

        List<Request> requestsToUpdate
                = requestRepository.findByEventIdAndIdIn(eventId, requestsIds);

        if (!requestsToUpdate.isEmpty()) {

            for (Request request : requestsToUpdate) {

                if (!request.getStatus().equals(RequestStatus.PENDING)) {
                    throw new ConflictException("Можно менять только статус PENDING");
                }

                if (event.getParticipantLimit() > event.getConfirmedRequests() &&
                        reqsUpdReq.getStatus().equals(CONFIRMED)) {

                    request.setStatus(CONFIRMED);

                    updateResponse.getConfirmedRequests()
                            .add(requestMapper.toDto(request));

                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);

                    eventRepository.save(event);

                } else {

                    request.setStatus(REJECTED);

                    updateResponse.getRejectedRequests()
                            .add(requestMapper.toDto(request));
                }

                requestRepository.save(request);
            }
        }

        return updateResponse;
    }
}
