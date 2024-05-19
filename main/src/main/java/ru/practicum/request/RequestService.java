package ru.practicum.request;

import ru.practicum.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.event.dto.EventRequestStatusUpdateResponse;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto create(Long userId, Long eventId);
    RequestDto cancel(Long userId, Long requestId);
    List<RequestDto> getAll(Long userId);
    List<RequestDto> getRequestsByInitiator(Long userId, Long eventId);
    EventRequestStatusUpdateResponse updateRequestsByInitiator(Long userId,
                                                               Long eventId,
                                                               EventRequestStatusUpdateRequest reqsUpdReq);
}
