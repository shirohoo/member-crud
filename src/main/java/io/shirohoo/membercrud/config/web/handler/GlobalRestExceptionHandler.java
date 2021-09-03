package io.shirohoo.membercrud.config.web.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.shirohoo.membercrud.exception.AlreadyExistsUsername;
import io.shirohoo.membercrud.web.model.HttpResponse;
import java.util.Iterator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return ResponseEntity.badRequest().body(HttpResponse.of(BAD_REQUEST, getResultMessage(ex)));
    }

    protected String getResultMessage(final MethodArgumentNotValidException ex) {
        final Iterator<ObjectError> iterator = ex.getBindingResult().getAllErrors().iterator();
        final StringBuilder resultMessageBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            final ObjectError objectError = iterator.next();
            resultMessageBuilder.append(objectError.getDefaultMessage());
            if (iterator.hasNext()) {
                resultMessageBuilder.append(", ");
            }
        }
        return resultMessageBuilder.toString();
    }

    @ExceptionHandler
    public ResponseEntity<HttpResponse<String>> errorHandle(final AlreadyExistsUsername e) {
        return ResponseEntity.status(CONFLICT)
            .body(HttpResponse.of(CONFLICT, e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<HttpResponse<String>> errorHandle(final UsernameNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
            .body(HttpResponse.of(NOT_FOUND, e.getMessage()));
    }
}
