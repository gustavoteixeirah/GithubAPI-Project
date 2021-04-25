package dev.gustavoteixeira.githubstats.api.exception.handler;

import dev.gustavoteixeira.githubstats.api.dto.ErrorResponseDTO;
import dev.gustavoteixeira.githubstats.api.exception.GitHubRepositoryNotFound;
import dev.gustavoteixeira.githubstats.api.exception.InvalidGitHubRepositoryURL;
import dev.gustavoteixeira.githubstats.api.exception.ProcessingError;
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
    protected ResponseEntity<ErrorResponseDTO> handleInvalidGitHubRepositoryURL(InvalidGitHubRepositoryURL e, WebRequest request) {
        ErrorResponseDTO error = e.error;
        error.date = ZonedDateTime.now();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(value
            = {GitHubRepositoryNotFound.class})
    protected ResponseEntity<ErrorResponseDTO> handleGitHubRepositoryNotFound(GitHubRepositoryNotFound e, WebRequest request) {
        ErrorResponseDTO error = e.error;
        error.date = ZonedDateTime.now();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(value
            = {IllegalStateException.class})
    protected ResponseEntity<ErrorResponseDTO> handleIllegalStateException(IllegalStateException e, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.builder()
                        .message("Internal Server Error.")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .date(ZonedDateTime.now()).build());
    }

    @ExceptionHandler(value
            = {ProcessingError.class})
    protected ResponseEntity<ErrorResponseDTO> handleProcessingError(ProcessingError e, WebRequest request) {
        ErrorResponseDTO error = e.error;
        error.date = ZonedDateTime.now();
        error.message = error.message.concat(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
