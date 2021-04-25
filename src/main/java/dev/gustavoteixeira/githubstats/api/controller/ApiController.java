package dev.gustavoteixeira.githubstats.api.controller;

import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.dto.GithubRepositoryDTO;
import dev.gustavoteixeira.githubstats.api.exception.InvalidGitHubRepositoryURL;
import dev.gustavoteixeira.githubstats.api.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Controller
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService service;

    @GetMapping("/")
    public ResponseEntity<List<ElementDTO>> getStatistics(@Validated @NotBlank @RequestBody GithubRepositoryDTO githubRepository) {
        logger.info("ApiController.getStatistics - Start - Input URL: {}", githubRepository.getUrl());
        List<ElementDTO> result = null;

        result = service.getRepositoryStatistics(githubRepository.getUrl());

        return ResponseEntity.ok(result);
    }

}
