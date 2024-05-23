package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.utils.Pattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Pattern.DATETIME);

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException exception) {

        log.error("Conflict exception: {}", exception.getMessage());
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistException(final AlreadyExistException exception) {

        log.error("Conflict exception: {}", exception.getMessage());
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException exception) {

        log.error("Not found exception: {}", exception.getMessage());

        return new ApiError(exception.getMessage(),
                "отсутствуют данные",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException exception) {

        log.error("Bad request exception: {}", exception.getMessage());
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {

        log.error("Bad request exception: {}", exception.getMessage());
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {

        log.error("Bad request exception: {}", exception.getMessage());
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {

        log.error("Not found exception: {}", exception.getMessage());
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }



}
