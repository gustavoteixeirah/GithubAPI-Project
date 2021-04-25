package dev.gustavoteixeira.githubstats.api.exception.handler;

import dev.gustavoteixeira.githubstats.api.dto.ErrorResponseDTO;
import dev.gustavoteixeira.githubstats.api.exception.InvalidGitHubRepositoryURL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {InvalidGitHubRepositoryURL.class})
    protected ResponseEntity<Object> handleInvalidGitHubRepositoryURL(InvalidGitHubRepositoryURL ex, WebRequest request) {
        ErrorResponseDTO error = ex.error;
        error.date = ZonedDateTime.now();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
