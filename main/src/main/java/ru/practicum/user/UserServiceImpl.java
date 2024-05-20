package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.AlreadyExistException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        validateUser(userDto);
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> get(List<Long> ids, Integer from, Integer size) {

        if (ids == null) {
            Pageable pageable = PageRequest.of(from, size);
            List<User> users = userRepository.findAll(pageable).toList();
            return userMapper.toDtoList(users);
        } else {
            List<User> users = userRepository.findAllById(ids);
            return userMapper.toDtoList(users);
        }

    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private void validateUser(UserDto userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new AlreadyExistException("пльзователь с таким именем уже существует");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistException("пользователь с такой почтой уже существует");
        }
    }
}
