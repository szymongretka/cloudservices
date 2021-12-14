package pl.sg.managment.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.sg.managment.exception.ApplicationNotFoundException;
import pl.sg.managment.exception.ErrorMessage;
import pl.sg.managment.exception.NoReasonException;
import pl.sg.managment.exception.WrongStateTransitionException;

import java.sql.Date;
import java.time.Instant;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ApplicationNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage applicationNotFoundException(ApplicationNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                Date.from(Instant.now()),
                ex.getMessage(),
                messageSource.getMessage("error.application.notfound", null, LocaleContextHolder.getLocale()));
        return message;
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage numberFormatException(NumberFormatException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                Date.from(Instant.now()),
                ex.getMessage(),
                messageSource.getMessage("error.incorrect.id", null, LocaleContextHolder.getLocale()));
        return message;
    }

    @ExceptionHandler(WrongStateTransitionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage wrongStateTransitionException(NumberFormatException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                Date.from(Instant.now()),
                ex.getMessage(),
                messageSource.getMessage("error.incorrect.state", null, LocaleContextHolder.getLocale()));
        return message;
    }

    @ExceptionHandler(NoReasonException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage reasonNotFoundException(NoReasonException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                Date.from(Instant.now()),
                ex.getMessage(),
                messageSource.getMessage("error.reason.notfound", null, LocaleContextHolder.getLocale()));
        return message;
    }

}
