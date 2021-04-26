package dev.gustavoteixeira.githubstats.api.service;

import dev.gustavoteixeira.githubstats.api.dto.Element;
import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.entity.ElementEntity;
import dev.gustavoteixeira.githubstats.api.entity.GithubRepositoryEntity;
import dev.gustavoteixeira.githubstats.api.exception.ProcessingError;
import dev.gustavoteixeira.githubstats.api.repository.ElementRepository;
import dev.gustavoteixeira.githubstats.api.repository.GithubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static dev.gustavoteixeira.githubstats.api.util.StringTransformationUtils.getRootRepository;
import static dev.gustavoteixeira.githubstats.api.util.TimeUtils.getTimeDifference;

@Service
public class ProcessorService {

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private GithubRepository githubRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProcessorService.class);

    public void persistElementInfo(String message) {
        logger.info("ElementRepository.persistElementInfo - Start - Message: {}", message);
        Element elementInfo = getElementInfo(message);
        String rootRepository = getRootRepository(elementInfo.getRepository());
        ElementEntity e = new ElementEntity();
        e.setRepository(elementInfo.getRepository());
        e.setRootRepository(rootRepository);
        e.setBytes(elementInfo.getBytes());
        e.setCount(elementInfo.getCount());
        e.setLines(elementInfo.getLines());
        e.setExtension(elementInfo.getExtension());
        elementRepository.save(e);
    }

    public static Element getElementInfo(String fullURL) {
        String normalizedURL = fullURL
                .replace("https://www.github.com", "https://raw.githubusercontent.com")
                .replace("blob/", "");
        logger.info("ProcessorService.getElementInfo - Start with URL: {}", normalizedURL);
        return getStatistics(normalizedURL);
    }

    public static Element getStatistics(String fullURL) {

        Element element = new Element();
        try (var inputStream = getUrl(fullURL).openStream()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            inputStream.transferTo(baos);
            element.setRepository(fullURL);
            element.setBytes(getFileBytes(new ByteArrayInputStream(baos.toByteArray())));
            element.setLines(getFileLines(new ByteArrayInputStream(baos.toByteArray())));
            element.setExtension(getFileExtension(fullURL));
            element.setCount(1);
        } catch (IOException e) {
            logger.error("ProcessorService.getStatistics - Error while trying to process the following url: {}", fullURL);
            throw new ProcessingError();
        }
        return element;
    }

    public static long getFileLines(InputStream inputStream) {
        long count = 0;
        try (var br = new BufferedReader(new InputStreamReader(inputStream))) {
            count = br.lines().count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static long getFileBytes(InputStream is) throws IOException {
        long size = 0;
        int chunk;
        byte[] buffer = new byte[1024];
        while ((chunk = is.read(buffer)) != -1) {
            size += chunk;
        }
        return size;
    }

    private static String getFileExtension(String fullURL) {
        String extension = fullURL.substring(fullURL.lastIndexOf("/") + 1);
        if (extension.contains(".")) {
            String[] split = extension.split("\\.");
            extension = split[split.length - 1];
        }
        return extension;
    }

    private static URL getUrl(String fullURL) {
        URL url = null;
        try {
            url = new URL(fullURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public void processStatisticsAndSaveElements(String rootRepository) {
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

    public List<ElementDTO> groupByExtension(List<ElementEntity> allByRootRepository, Collection<String> extensions) {
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

