package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid  UserDto userDto) {
        log.info("\n\nсоздание пользователя\n");
        return userService.create(userDto);
    }

    @GetMapping
    public List<UserDto> get(@RequestParam(required = false, name = "ids") List<Long> ids,
                             @RequestParam(name = "from", defaultValue = "0") Integer from,
                             @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("\n\nполучение пользователя\n");
        return userService.get(ids, from, size);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("\n\nудаление пользователя\n");
        userService.delete(id);
    }
}
