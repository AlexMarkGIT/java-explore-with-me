package ru.practicum.comment;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentNewDto;
import ru.practicum.comment.dto.CommentPublicDto;
import ru.practicum.comment.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    CommentDto create(CommentNewDto dto, Long userId);

    CommentDto update(CommentUpdateDto dto, Long userId, Long commentId);

    CommentDto confirm(Long commentId, Boolean confirmed);

    void delete(Long userId, Long commentId);

    CommentDto get(Long userId, Long commentId);

    List<CommentDto> getAll(Long userId);

    List<CommentPublicDto> getAllByEvent(Long eventId,
                                         Integer from,
                                         Integer size);
}
