package dev.gustavoteixeira.githubstats.api.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Builder
public class ErrorResponseDTO {

    public HttpStatus status;
    public String message;
    public ZonedDateTime date;

}
