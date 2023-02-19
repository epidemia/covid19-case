package com.scentbird.covidcase.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Covid-19 Statistic")
public class Covid19Statistic {

    @Schema(description = "Aggregation Type", required = true)
    private final AggregationType aggregationType;

    @Schema(description = "Country", example = "Portugal", required = true)
    private String country;

    @Schema(description = "Date", required = true)
    private LocalDate date;

    @Schema(description = "Cases", required = true)
    private Long cases;

    public Covid19Statistic(AggregationType aggregationType, String country, LocalDate date, Long cases) {
        this.aggregationType = aggregationType;
        this.country = country;
        this.date = date;
        this.cases = cases;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCases() {
        return cases;
    }

    public void setCases(Long cases) {
        this.cases = cases;
    }
}
