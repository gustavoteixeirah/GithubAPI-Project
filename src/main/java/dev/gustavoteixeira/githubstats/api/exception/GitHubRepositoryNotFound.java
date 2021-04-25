package dev.gustavoteixeira.githubstats.api.exception;

import dev.gustavoteixeira.githubstats.api.dto.ErrorResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
public class GitHubRepositoryNotFound extends RuntimeException {

    public ErrorResponseDTO error = ErrorResponseDTO.builder()
            .message("GitHub repository not found.")
            .status(HttpStatus.NOT_FOUND).build();

}