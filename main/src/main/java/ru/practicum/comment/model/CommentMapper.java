package ru.practicum.comment.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentPublicDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "commentator", source = "commentator.id")
    @Mapping(target = "event", source = "event.id")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);

    @Mapping(target = "commentator", source = "commentator.id")
    @Mapping(target = "event", source = "event.id")
    CommentPublicDto toPublicDto(Comment comment);

    List<CommentPublicDto> toPublicDtoList(List<Comment> comments);
}
