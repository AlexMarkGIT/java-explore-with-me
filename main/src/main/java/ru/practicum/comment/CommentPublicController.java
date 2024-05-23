package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentPublicDto;

import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping("/events/{eventId}")
    private List<CommentPublicDto> getAllByEvent(@PathVariable Long eventId,
                                                 @RequestParam(defaultValue = "0",
                                                         required = false) Integer from,
                                                 @RequestParam(defaultValue = "10",
                                                         required = false) Integer size) {
        log.info("получение всех комментариев на событие");
        return commentService.getAllByEvent(eventId, from, size);
    }
}
