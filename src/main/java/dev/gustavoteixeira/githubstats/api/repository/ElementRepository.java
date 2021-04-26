package dev.gustavoteixeira.githubstats.api.repository;

import dev.gustavoteixeira.githubstats.api.entity.ElementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementRepository extends MongoRepository<ElementEntity, String> {

    List<ElementEntity> findAllByRootRepository(String rootRepository);

}
