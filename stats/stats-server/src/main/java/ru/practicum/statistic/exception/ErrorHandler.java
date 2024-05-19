package ru.practicum.statistic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ErrorHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTimePeriodException(final TimePeriodException exception) {
        return new ApiError(exception.getMessage(),
                "некорректно составленный запрос",
                HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                LocalDateTime.now().format(formatter));
    }

}
