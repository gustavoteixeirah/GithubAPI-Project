package dev.gustavoteixeira.githubstats.api.controller;

import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.dto.GithubRepositoryDTO;
import dev.gustavoteixeira.githubstats.api.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@SuppressWarnings("ConstantConditions")
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService service;

    @GetMapping("/")
    public ResponseEntity<List<ElementDTO>> getStatistics(@RequestBody GithubRepositoryDTO githubRepository) {
        // TODO Validate input / sanitize / @Validated
        logger.info("ApiController.getStatistics - Start - Input URL: {}", githubRepository.getUrl());
        List<ElementDTO> result = null;

        try {
            result = service.getRepositoryStatistics(githubRepository.getUrl());
        } catch (Exception e) {
            logger.error("ApiController.getStatistics - Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(result);
    }

}
