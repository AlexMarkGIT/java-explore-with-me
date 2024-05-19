package ru.practicum.user.model;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    List<UserDto> toDtoList(List<User> users);
}
