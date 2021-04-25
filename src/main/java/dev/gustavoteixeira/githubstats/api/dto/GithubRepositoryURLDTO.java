package dev.gustavoteixeira.githubstats.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GithubRepositoryURLDTO {
    @ApiModelProperty()
    @NotBlank(message = "GitHub repository URL is mandatory!")
    private String url;

}
