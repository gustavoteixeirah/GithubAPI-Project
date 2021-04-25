package dev.gustavoteixeira.githubstats.api.repository;

import dev.gustavoteixeira.githubstats.api.entity.GithubRepositoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubRepository extends MongoRepository<GithubRepositoryEntity, String> {
    GithubRepositoryEntity findByRootRepository(String url);
}
