package com.scentbird.covidcase.service.covid19client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record Covid19Case(
    @JsonProperty("Country") String country,
    @JsonProperty("Cases") Long cases,
    @JsonProperty("Date") Instant date,
    @JsonProperty("Province") String province
) {

    public Covid19Case(
        @JsonProperty("Country") String country,
        @JsonProperty("Cases") Long cases,
        @JsonProperty("Date") Instant date) {
        this(country, cases, date, "");
    }

}
