package dev.gustavoteixeira.githubstats.api.controller;

import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.dto.GithubRepositoryURLDTO;
import dev.gustavoteixeira.githubstats.api.service.ApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;

@Controller
public class ApiController {

    Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApiService service;

    @ApiOperation(value = "Returns the file count, the total number of lines" +
            " and the total number of bytes grouped by file extension, of a given public Github repository",
            notes = "The url MUST be in the following format: https://github.com/OWNER/REPOSITORY .\n " +
                    "For example: https://github.com/iwhrim/GithubAPI-microservice .\n" +
                    "Where the owner is \"iwhrim\" and the repository is \"GithubAPI-microservice\".")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "The repository does not have any file.", response = Object.class),
            @ApiResponse(code = 400, message = "This is not a valid GitHub repository URL. Please check the documentation."),
            @ApiResponse(code = 404, message = "GitHub repository not found."),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ElementDTO>> getStatistics(
            @ApiParam(value = "URL of the GitHub repository", required = true,
                    example = "https://github.com/iwhrim/qa-ninja-automacao-180")
            @Validated @RequestBody GithubRepositoryURLDTO githubRepository) {
        logger.info("ApiController.getStatistics - Start - Input URL: {}", githubRepository.getUrl());

        List<ElementDTO> result = service.getRepositoryStatistics(githubRepository.getUrl());

        return CollectionUtils.isEmpty(result) ? ResponseEntity.noContent().build() : ResponseEntity.ok(result) ;
    }

}
