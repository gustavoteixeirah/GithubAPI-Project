package dev.gustavoteixeira.githubstats.api.controller;

import dev.gustavoteixeira.githubstats.api.dto.ElementDTO;
import dev.gustavoteixeira.githubstats.api.dto.GithubRepositoryURLDTO;
import dev.gustavoteixeira.githubstats.api.service.ApiService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "Exclusão efetuada com sucesso")
//    })
    @GetMapping("/statistics")
    public ResponseEntity<List<ElementDTO>> getStatistics(@Validated @RequestBody GithubRepositoryURLDTO githubRepository) {
        logger.info("ApiController.getStatistics - Start - Input URL: {}", githubRepository.getUrl());
        List<ElementDTO> result = null;

        result = service.getRepositoryStatistics(githubRepository.getUrl());

        return ResponseEntity.ok(result);
    }

}
