package dev.gustavoteixeira.githubstats.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ElementDTO {
    public String extension;
    public int count;
    public long lines;
    public long bytes;
}
