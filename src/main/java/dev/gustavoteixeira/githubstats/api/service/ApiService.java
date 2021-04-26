package dev.gustavoteixeira.githubstats.api.service;

import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.entity.ElementEntity;
import dev.gustavoteixeira.githubstats.api.entity.GithubRepositoryEntity;
import dev.gustavoteixeira.githubstats.api.exception.ProcessingError;
import dev.gustavoteixeira.githubstats.api.repository.ElementRepository;
import dev.gustavoteixeira.githubstats.api.repository.GithubRepository;
import dev.gustavoteixeira.githubstats.api.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static dev.gustavoteixeira.githubstats.api.util.StringTransformationUtils.getRootRepository;
import static dev.gustavoteixeira.githubstats.api.util.TimeUtils.getTimeDifference;

@Service
public class ApiService {

    private static Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    private GithubRepository githubRepository;

    @Autowired
    private ElementRepository elementRepository;

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
            int quantityOfMappedElements = webUtils.mapRepository(repositoryURL);

            // returs the total of elements that that repository contains
            //Obs: "element" is the name I gave to describe a file in that repository
            WebUtils.count = 0;

            // Get the total elements that are saved in the database
            var totalElements = elementRepository.countByRootRepository(rootRepository);

            // Creates a variable to hold the total time that the thread will sleep
            long threadSleepingTime = 0;

            // Checks if the total saved elements is equal to the quantity of mapped elements
            while (quantityOfMappedElements != totalElements) {

                logger.debug("ApiService.getRepositoryStatistics - quantityOfMappedElements: {} - totalElements : {}", quantityOfMappedElements, totalElements);
                // Updates the total elements variable
                totalElements = elementRepository.countByRootRepository(rootRepository);

                // Wait a few milliseconds (usually it sleeps between 250 - 400 milliseconds)
                if (threadSleepingTime == 0)
                    logger.info("ApiService.getRepositoryStatistics - Thread will start to sleep");
                try {
                    Thread.sleep(50);
                    threadSleepingTime += 50;
                } catch (InterruptedException e) {
                    logger.error("ApiService.getRepositoryStatistics - Error - Error while trying to get the thread to sleep: {}", e.getMessage());
                    throw new ProcessingError();
                }
            }

            logger.info("ApiService.getRepositoryStatistics - Thread finished, slept for {} seconds", threadSleepingTime);
            // Gets out of the loop after all elements have been mapped, then process the statistics and save
            processStatisticsAndSaveElements(rootRepository);

            // Gets the newly processed repository
            repository = githubRepository.findByRootRepository(rootRepository);
            logger.info("ApiService.getRepositoryStatistics - Finished mapping the repository: {}", rootRepository);
        }

        //Get the elements DTOs and return
        return repository.getElements();
    }

    private void processStatisticsAndSaveElements(String rootRepository) {
        List<ElementEntity> allByRootRepository = elementRepository.findAllByRootRepository(rootRepository);

        // Get files extensions
        Collection<String> extensions = new HashSet<>();
        allByRootRepository.forEach(e -> extensions.add(e.getExtension()));

        // Group the elements by its extension
        List<ElementDTO> groupedElements = groupByExtension(allByRootRepository, extensions);

        // Save the elements in the database
        githubRepository.save(
                GithubRepositoryEntity.builder()
                        .rootRepository(rootRepository)
                        .elements(groupedElements)
                        .build());
    }

    private List<ElementDTO> groupByExtension(List<ElementEntity> allByRootRepository, Collection<String> extensions) {
        logger.debug("ApiService.groupByExtension - Start - Grouping by extension");
        long start = System.currentTimeMillis();
        List<ElementDTO> groupedElements = new ArrayList<>();

        extensions.forEach(extension -> {
            List<ElementDTO> elementsOfOneExtension = allByRootRepository.stream().filter(element -> element.getExtension().equals(extension)).collect(Collectors.toList());
            int count = elementsOfOneExtension.stream().mapToInt(ElementDTO::getCount).sum();
            long lines = elementsOfOneExtension.stream().mapToLong(ElementDTO::getLines).sum();
            long bytes = elementsOfOneExtension.stream().mapToLong(ElementDTO::getBytes).sum();
            groupedElements.add(ElementDTO.builder()
                    .extension(extension)
                    .count(count)
                    .lines(lines)
                    .bytes(bytes)
                    .build());

        });
        long end = System.currentTimeMillis();
        logger.debug("ApiService.groupByExtension - End - Grouping by extension time: {}", getTimeDifference(start, end));

        return groupedElements;
    }

}
