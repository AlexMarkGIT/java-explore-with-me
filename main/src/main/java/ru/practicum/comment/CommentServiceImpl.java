package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentNewDto;
import ru.practicum.comment.dto.CommentPublicDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentMapper;
import ru.practicum.event.EventRepository;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper mapper;

    @Override
    public CommentDto create(CommentNewDto dto, Long userId) {

        User commentator = getUserById(userId);
        Event event = getEventById(dto.getEvent());

        checkEventIsPublished(event);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .created(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .commentator(commentator)
                .event(event)
                .build();

        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(CommentUpdateDto dto, Long userId, Long commentId) {

        User user = getUserById(userId);

        Comment comment = getCommentById(commentId);

        if (!comment.getCommentator().getId().equals(user.getId())) {
            throw new ConflictException("Пользователь не является владельцем комментария");
        }

        if (comment.getStatus().equals(CommentStatus.REJECTED)) {
            throw new ConflictException("нельзя обновлять отклоненные комментарии");
        }

        comment.setContent(dto.getContent());
        comment.setStatus(CommentStatus.PENDING);

        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto confirm(Long commentId, Boolean confirmed) {

        Comment comment = getCommentById(commentId);

        if (!comment.getStatus().equals(CommentStatus.PENDING)) {
            throw new ConflictException("комментарий уже прошел модерацию администратором");
        }

        if (confirmed) {
            comment.setStatus(CommentStatus.CONFIRMED);
            comment.setPublished(LocalDateTime.now());
        } else {
            comment.setStatus(CommentStatus.REJECTED);
        }

        return mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Long userId, Long commentId) {

        User user = getUserById(userId);
        Comment comment = getCommentById(commentId);

        if (!comment.getCommentator().getId().equals(user.getId())) {
            throw new ConflictException("Пользователь не является владельцем комментария");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto get(Long userId, Long commentId) {

        User user = getUserById(userId);
        Comment comment = getCommentById(commentId);

        if (!comment.getCommentator().getId().equals(user.getId())) {
            throw new ConflictException("Пользователь не является владельцем комментария");
        }

        return mapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getAll(Long userId) {

        User user = getUserById(userId);

        List<Comment> comments = commentRepository.findAllByCommentator(user);

        return mapper.toDtoList(comments);
    }

    @Override
    public List<CommentPublicDto> getAllByEvent(Long eventId, Integer from, Integer size) {

        Event event = getEventById(eventId);

        checkEventIsPublished(event);

        List<Comment> comments = commentRepository
                .findAllByEventAndStatus(event,
                        CommentStatus.CONFIRMED,
                        PageRequest.of(from / size, size));

        return mapper.toPublicDtoList(comments);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя не существует"));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("такого события не существует"));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("такого комментария не существует"));
    }

    public void checkEventIsPublished(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("событие еще не опубликовано");
        }
    }

}
