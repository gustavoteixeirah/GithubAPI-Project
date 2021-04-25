package dev.gustavoteixeira.githubstats.api.service;

import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.entity.ElementEntity;
import dev.gustavoteixeira.githubstats.api.entity.GithubRepositoryEntity;
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

@Service
public class ApiService {

    private static Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    private GithubRepository githubRepository;

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private WebUtils webUtils;

    public List<ElementDTO> getRepositoryStatistics(String repositoryURL) {
        logger.info("ApiService.getRepositoryStatistics - Start");

//        List<ElementDTO> elements = DynamoDBService.retrieveItem(githubRepository.getUrl());
        List<ElementDTO> elements = new ArrayList<>();

        // Ver se a URL já está mapeada no DB
        String rootRepository = getRootRepository(repositoryURL);

        GithubRepositoryEntity repository = githubRepository.findByRootRepository(rootRepository);
        // Se não
        if (repository == null) {

            logger.info("ApiService.getRepositoryStatistics - Mapping repository");
            int items = webUtils.mapRepository(repositoryURL);
            WebUtils.count = 0;

            logger.info("ApiService.getRepositoryStatistics - Finding all elements by root repository");
            var totalElements = elementRepository.countByRootRepository(rootRepository);
            logger.info("ApiService.getRepositoryStatistics - Processing - Total of already processed elements: {}", totalElements);
            // verifica se file count == total de elementos
            while (items != totalElements) {
                totalElements = elementRepository.countByRootRepository(rootRepository);
                // Espera 2min ~ 4min
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    logger.error("ApiService.getRepositoryStatistics - Error - Error while trying to get the thread to sleep");
                }
            }
            logger.info("ApiService.getRepositoryStatistics - Processing - Total of already processed elements: {}", totalElements);
            // Busca o Element
            List<ElementEntity> allByRootRepository = elementRepository.findAllByRootRepository(rootRepository);

            // elements = Converte para um List<ElementDTO>
            List<ElementDTO> finalElements1 = elements;
            allByRootRepository.forEach(elementEntity -> {
                finalElements1.add(ElementDTO.builder()
                        .extension(elementEntity.getExtension())
                        .bytes(elementEntity.getBytes())
                        .lines(elementEntity.getLines())
                        .count(elementEntity.getCount()).build());
            });
            //Pega as extensões
            Collection<String> extensions = new HashSet<>();
            allByRootRepository.forEach(e -> extensions.add(e.getExtension()));

            // Agrupa os elementos de mesma extensao
            List<ElementDTO> groupedElements = new ArrayList<>();
            List<ElementDTO> finalElements = elements;
            extensions.forEach(extension -> {
                List<ElementDTO> elementsOfOneExtension = finalElements.stream().filter(element -> element.getExtension().equals(extension)).collect(Collectors.toList());
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
            elements = groupedElements;
            logger.info("ApiService.getRepositoryStatistics - Finished");
            githubRepository.save(GithubRepositoryEntity.builder().rootRepository(rootRepository).elements(elements).build());
        } else {
            elements = repository.getElements();
        }
        return elements;
    }


}
