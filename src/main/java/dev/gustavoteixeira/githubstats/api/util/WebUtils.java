package dev.gustavoteixeira.githubstats.api.util;

import dev.gustavoteixeira.githubstats.api.dto.UnprocessedElementDTO;
import dev.gustavoteixeira.githubstats.api.exception.GitHubRepositoryNotFound;
import dev.gustavoteixeira.githubstats.api.service.ProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static dev.gustavoteixeira.githubstats.api.util.TimeUtils.getTimeDifference;
import static org.apache.commons.lang3.StringUtils.substringBetween;

@Service
public class WebUtils {
    private static Logger logger = LoggerFactory.getLogger(WebUtils.class);
    public static int count = 0;

    @Autowired
    private ProcessorService processorService;

    public int mapRepository(String repositoryURL) {
        logger.info("WebUtils.mapRepository - Start - Processing");
        long start = System.currentTimeMillis();

        List<String> elementListOfTheFirstPage = getGithubRepositoryElementsList(repositoryURL);
        //TODO Check se a elementListOfTheFirstPage não é null
        List<UnprocessedElementDTO> remainingDirectories = transformRawElementsToUnprocessedElements(elementListOfTheFirstPage);

        if (!CollectionUtils.isEmpty(remainingDirectories)) {
            recursive(remainingDirectories);
        }

        long end = System.currentTimeMillis();
        logger.info("WebUtils.mapRepository - End - Processing time: {}", getTimeDifference(start, end));
        return count;
    }

    public void recursive(List<UnprocessedElementDTO> remainingDirectory) {
        remainingDirectory.forEach(unprocessedElementDTO -> {
            List<String> elementListOfTheFirstPage = getGithubRepositoryElementsList(unprocessedElementDTO.getAddress());
            List<UnprocessedElementDTO> newlyRemainingDirectory = transformRawElementsToUnprocessedElements(elementListOfTheFirstPage);
            if (!CollectionUtils.isEmpty(newlyRemainingDirectory)) {
                recursive(newlyRemainingDirectory);
            }
        });
    }

    public static List<UnprocessedElementDTO> getUnprocessedElementsDTO(List<String> elementList) {
        List<UnprocessedElementDTO> unprocessedElementsList = new ArrayList<>();

        elementList.forEach(element -> {
            if (element != null && !element.isBlank()) {
                String elementType = element.contains("blob") ? "File" : "Directory";
                String elementAddress = "https://www.github.com" + substringBetween(element, "href=\"", "\">");
                UnprocessedElementDTO e = new UnprocessedElementDTO();
                e.setType(elementType);
                e.setAddress(elementAddress);
                unprocessedElementsList.add(e);
            }
        });

        return unprocessedElementsList;
    }

    public List<UnprocessedElementDTO> transformRawElementsToUnprocessedElements(List<String> elementListOfTheFirstPage) {
        List<UnprocessedElementDTO> filesAndDirectories = getUnprocessedElementsDTO(elementListOfTheFirstPage);

        filesAndDirectories.forEach(element -> {
            if ((element.getType()).equals("File")) {
                logger.info("WebUtils.transformRawElementsToUnprocessedElements - Processing - Starting new thread with url: {}", element.getAddress());
                new Thread(() -> processorService.persistElementInfo(element.getAddress())).start();
                count++;
            }
        });

        filesAndDirectories.removeIf(e -> e.getType().equals("File"));
        return filesAndDirectories;
    }

    public static List<String> getGithubRepositoryElementsList(String fullURL) {
        List<String> elements = new ArrayList<>();

        try (var br = new BufferedReader(new InputStreamReader(getUrl(fullURL).openStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("data-pjax=\"#repo-content-pjax-container\"")) {
                    elements.add(line);
                }
            }
        } catch (IOException e) {
            logger.error("WebUtils.getGithubRepositoryElementsList - Error - URL: {}", fullURL);
            throw new GitHubRepositoryNotFound();
        }

        return elements;
    }

    private static URL getUrl(String fullURL) {
        URL url = null;
        try {
            url = new URL(fullURL);
        } catch (MalformedURLException e) {
            logger.error("WebUtils.getUrl - Error - MalformedURLException - URL: {}", fullURL);
            throw new RuntimeException();
        }

        return url;
    }

}