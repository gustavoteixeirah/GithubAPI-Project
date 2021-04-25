package dev.gustavoteixeira.githubstats.api.exception;

import dev.gustavoteixeira.githubstats.api.dto.ErrorResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@NoArgsConstructor
public class ProcessingError extends RuntimeException {

    public ErrorResponseDTO error = ErrorResponseDTO.builder()
            .message(this.getMessage())
            .status(HttpStatus.INTERNAL_SERVER_ERROR).build();

}
