package dev.gustavoteixeira.githubstats.api.entity;

import dev.gustavoteixeira.githubstats.api.dto.Element;
import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Document(collection = "github_repository")
@Builder
@Getter
@Setter
public class GithubRepositoryEntity {

    @Id
    public String rootRepository;
    public List<ElementDTO> elements;

}
