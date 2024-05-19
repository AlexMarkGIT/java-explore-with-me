package ru.practicum.user;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    List<UserDto> get(List<Long> ids, Integer from, Integer size);

    void delete(Long id);
}
