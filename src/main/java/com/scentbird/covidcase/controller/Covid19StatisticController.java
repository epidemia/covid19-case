package com.scentbird.covidcase.controller;

import com.scentbird.covidcase.dto.AggregationType;
import com.scentbird.covidcase.dto.Covid19Statistic;
import com.scentbird.covidcase.service.Covid19StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/covid19-statistics")
@Tag(name = "covid19-statistics")
public class Covid19StatisticController {

    private final Covid19StatisticService covid19StatisticService;

    public Covid19StatisticController(Covid19StatisticService covid19StatisticService) {
        this.covid19StatisticService = covid19StatisticService;
    }

    @Operation(summary = "Returns statistics on confirmed cases")
    @GetMapping("/confirmed")
    public List<Covid19Statistic> getConfirmedCasesStatistics(
        @Parameter(description = "Aggregation Types", example = "MIN", required = true)
        @NotEmpty @RequestParam Set<AggregationType> aggregationTypes,
        @Parameter(description = "Countries", example = "Portugal", required = true)
        @NotEmpty @RequestParam Set<String> countries,
        @Parameter(description = "From (Inclusive). Must be less than current day", required = true)
        @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @Parameter(description = "To (Inclusive). Must be less than current day", required = true)
        @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (from.isAfter(to) ||
            from.isAfter(LocalDate.now().minusDays(1)) ||
            to.isAfter(LocalDate.now().minusDays(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid time interval.");
        }
        return covid19StatisticService.getConfirmedCasesStatistics(aggregationTypes, countries, from, to);
    }

}
