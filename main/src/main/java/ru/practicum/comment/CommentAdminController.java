package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto confirm(@PathVariable Long commentId,
                              @RequestParam Boolean confirmed) {
        log.info("\nподтверждение или отклонение публикации комментария\n");
        return commentService.confirm(commentId, confirmed);
    }
}
