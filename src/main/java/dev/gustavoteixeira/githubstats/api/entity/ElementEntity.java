package dev.gustavoteixeira.githubstats.api.entity;

import dev.gustavoteixeira.githubstats.api.dto.Element;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "element_entity")
@Getter
@Setter
public class ElementEntity extends Element {

    @Id
    public String repository;
    public String rootRepository;

}
