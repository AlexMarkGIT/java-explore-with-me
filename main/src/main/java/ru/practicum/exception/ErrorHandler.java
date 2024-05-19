package ru.practicum.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException exception) {
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExistException(final AlreadyExistException exception) {
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.CONFLICT.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException exception) {
        return new ApiError(exception.getMessage(),
                "отсутствуют данные",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException exception) {
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleEmptyResultDataAccessException(final EmptyResultDataAccessException exception) {
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }



}
