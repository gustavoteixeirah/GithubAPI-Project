package dev.gustavoteixeira.githubstats.api.service;

import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.entity.GithubRepositoryEntity;
import dev.gustavoteixeira.githubstats.api.exception.ProcessingError;
import dev.gustavoteixeira.githubstats.api.repository.GithubRepository;
import dev.gustavoteixeira.githubstats.api.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.gustavoteixeira.githubstats.api.util.StringTransformationUtils.getRootRepository;

@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    private GithubRepository githubRepository;

    @Autowired
    private ProcessorService processorService;

    @Autowired
    private WebUtils webUtils;

    public List<ElementDTO> getRepositoryStatistics(String repositoryURL) throws ProcessingError {
        logger.info("ApiService.getRepositoryStatistics - Start");


        // Gets the repository in the following format: <OWNER>/<REPOSITORY_NAME>
        String rootRepository = getRootRepository(repositoryURL);

        // Checks if the repository is already mapped on the database
        GithubRepositoryEntity repository = githubRepository.findByRootRepository(rootRepository);

        // If it is not
        if (repository == null) {
            logger.info("ApiService.getRepositoryStatistics - Started mapping repository");

            // Then map the repository (internally using threads)
            webUtils.mapRepository(repositoryURL);

            // Process the statistics and save it to the database
            processorService.processStatisticsAndSaveElements(rootRepository);

            // Gets the newly processed repository
            repository = githubRepository.findByRootRepository(rootRepository);
            logger.info("ApiService.getRepositoryStatistics - Finished mapping the repository: {}", rootRepository);
        }

        //Get the elements DTOs and return
        return repository.getElements();
    }

}
