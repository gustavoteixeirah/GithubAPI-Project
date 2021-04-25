package dev.gustavoteixeira.githubstats.api.service;

import dev.gustavoteixeira.githubstats.api.dto.Element;
import dev.gustavoteixeira.githubstats.api.entity.ElementEntity;
import dev.gustavoteixeira.githubstats.api.repository.ElementRepository;
import dev.gustavoteixeira.githubstats.api.repository.GithubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static dev.gustavoteixeira.githubstats.api.util.StringTransformationUtils.getRootRepository;


@Service
public class ProcessorService {

    @Autowired
    private ElementRepository elementRepository;

    @Autowired
    private GithubRepository repository;

    private static Logger logger = LoggerFactory.getLogger(ProcessorService.class);

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
        logger.info("ElementRepository.persistElementInfo - Finished {}", elementInfo.getRepository());
    }

    public static Element getElementInfo(String fullURL) {
        String normalizedURL = fullURL
                .replace("https://www.github.com", "https://raw.githubusercontent.com")
                .replace("blob/", "");
        logger.info("SQSService.getElementInfo - Start with URL: {}", normalizedURL);
        return getStatistics(normalizedURL);
    }

    public static Element getStatistics(String fullURL) {
        String result = "";
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
            e.printStackTrace();
            //TODO clear data of the root repository fullURL

            //TODO log error

            //TODO Throw error and return 500 internal server error

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
        int chunk = 0;
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

}

