package dev.gustavoteixeira.githubstats.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GithubRepositoryDTO implements Serializable {

    @NotBlank(message = "GitHub repository URL is mandatory!")
    private String url;

    private List<Element> elements;
}
