package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentNewDto;
import ru.practicum.comment.dto.CommentUpdateDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@RequestBody @Valid CommentNewDto dto,
                             @PathVariable Long userId) {

        log.info("\nсоздание комментария\n");
        System.out.println("\n");
        System.out.println(dto);
        System.out.println("\n");

        CommentDto resp = commentService.create(dto, userId);

        System.out.println("\n");
        System.out.println(resp);
        System.out.println("\n");

        return resp;
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@RequestBody @Valid CommentUpdateDto dto,
                             @PathVariable Long userId,
                             @PathVariable Long commentId) {

        log.info("\nобновление комментария\n");
        return commentService.update(dto, userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {

        log.info("\nудаление комментария\n");
        commentService.delete(userId, commentId);
    }

    @GetMapping
    public List<CommentDto> getAll(@PathVariable Long userId) {

        log.info("\nполучение комментариев коментатором\n");
        return commentService.getAll(userId);
    }

    @GetMapping("/{commentId}")
    public CommentDto get(@PathVariable Long userId,
                          @PathVariable Long commentId) {

        log.info("\nполучение одного комментария коментатором\n");
        return commentService.get(userId, commentId);
    }
}
