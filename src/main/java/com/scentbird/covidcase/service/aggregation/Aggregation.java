package com.scentbird.covidcase.service.aggregation;

import com.scentbird.covidcase.dto.AggregationType;
import com.scentbird.covidcase.dto.Covid19Statistic;

import java.time.LocalDate;
import java.util.Set;

public interface Aggregation {

    Covid19Statistic aggregate(Set<String> countries, LocalDate from, LocalDate to);

    AggregationType getType();

}
