package com.scentbird.covidcase.service;

import com.scentbird.covidcase.dto.AggregationType;
import com.scentbird.covidcase.dto.Covid19Statistic;
import com.scentbird.covidcase.service.aggregation.Aggregation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class Covid19StatisticService {

    private final Map<AggregationType, Aggregation> aggregationServiceByAggregationType;

    private final CountryService countryService;

    public Covid19StatisticService(List<Aggregation> aggregations, CountryService countryService) {
        this.aggregationServiceByAggregationType = aggregations.stream()
            .collect(Collectors.toMap(Aggregation::getType, Function.identity()));
        this.countryService = countryService;
    }

    public List<Covid19Statistic> getConfirmedCasesStatistics(
        Set<AggregationType> aggregationTypes,
        Set<String> countries,
        LocalDate from,
        LocalDate to) {
        var slugByCountryMap = countryService.getSlugByCountry();
        checkThatAllCountriesFound(countries, slugByCountryMap);

        var slugs = countries.stream()
            .map(slugByCountryMap::get)
            .collect(Collectors.toSet());

        return aggregationTypes.stream()
            .map(aggregationType -> {
                var aggregationService = aggregationServiceByAggregationType.get(aggregationType);
                if (aggregationService == null) {
                    throw new IllegalStateException(
                        "Not found aggregation service for aggregationType: " + aggregationType
                    );
                }
                return aggregationService.aggregate(slugs, from, to);
            })
            .toList();
    }

    private static void checkThatAllCountriesFound(Set<String> countries, Map<String, String> slugByCountryMap) {
        var notFoundCountries = countries.stream()
            .filter(country -> !slugByCountryMap.containsKey(country))
            .toList();
        if (!notFoundCountries.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "The next countries not found: " + notFoundCountries
            );
        }
    }

}
