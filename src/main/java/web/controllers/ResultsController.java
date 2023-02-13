package web.controllers;

import dto.StatisticsDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import service.api.IStatisticsService;


@RestController
public class ResultsController {

    private final IStatisticsService service;

    public ResultsController(IStatisticsService service) {
        this.service = service;
    }

    @GetMapping("/results")
    public StatisticsDTO getResults() {
        return this.service.getStatistics();
    }
}
