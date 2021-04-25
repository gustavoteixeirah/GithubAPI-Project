package dev.gustavoteixeira.githubstats.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UnprocessedElementDTO {
    String type;
    String address;
}
