package dev.gustavoteixeira.githubstats.api.exception;

import dev.gustavoteixeira.githubstats.api.dto.ErrorResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
public class InvalidGitHubRepositoryURL extends RuntimeException {

    public ErrorResponseDTO error = ErrorResponseDTO.builder()
            .message("This is not a valid GitHub repository URL. Please check the documentation.")
            .status(HttpStatus.BAD_REQUEST).build();

}
