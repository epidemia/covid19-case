package com.scentbird.covidcase.service.aggregation;

import com.scentbird.covidcase.dto.AggregationType;
import com.scentbird.covidcase.dto.Covid19Statistic;
import com.scentbird.covidcase.service.covid19client.CacheableCovid19Client;
import com.scentbird.covidcase.service.covid19client.Covid19Case;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Set;
import java.util.function.BiFunction;

public abstract class AbstractAggregation implements Aggregation {

    private final CacheableCovid19Client covid19Client;

    private final AggregationType aggregationType;

    private final BiFunction<Long, Long, Boolean> comparisonFunction;

    protected AbstractAggregation(
        CacheableCovid19Client covid19Client,
        AggregationType aggregationType,
        BiFunction<Long, Long, Boolean> comparisonFunction) {
        this.covid19Client = covid19Client;
        this.aggregationType = aggregationType;
        this.comparisonFunction = comparisonFunction;
    }

    @Override
    public Covid19Statistic aggregate(Set<String> countries, LocalDate from, LocalDate to) {
        Covid19Statistic result = null;
        for (String country : countries) {
            var response = covid19Client.getConfirmedCases(
                    country,
                    from.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC),
                    to.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)
                ).stream()
                .filter(covid19Case -> covid19Case.province() == null || covid19Case.province().isBlank())
                .sorted(Comparator.comparing(Covid19Case::date))
                .toList();
            if (response.isEmpty()) {
                continue;
            }

            for (int i = 1; i < response.size(); i++) {
                var newCases = response.get(i).cases() - response.get(i - 1).cases();
                if (result == null) {
                    result = new Covid19Statistic(
                        aggregationType,
                        response.get(i).country(),
                        response.get(i).date().atZone(ZoneId.of("UTC")).toLocalDate(),
                        newCases
                    );
                } else if (comparisonFunction.apply(newCases, result.getCases())) {
                    result.setCases(newCases);
                    result.setCountry(response.get(i).country());
                    result.setDate(response.get(i).date().atZone(ZoneId.of("UTC")).toLocalDate());
                }
            }
        }

        return result;
    }

    @Override
    public AggregationType getType() {
        return aggregationType;
    }
}
