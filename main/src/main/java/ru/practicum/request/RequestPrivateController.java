package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("\n\nсоздание запроса на участие\n");
        return requestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("\n\nотмена запроса на участие\n");
        return requestService.cancel(userId, requestId);
    }

    @GetMapping
    public List<RequestDto> getAll(@PathVariable Long userId) {
        log.info("\n\nполучение всех запросов участника\n");
        return requestService.getAll(userId);
    }
}
