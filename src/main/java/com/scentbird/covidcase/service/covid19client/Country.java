package com.scentbird.covidcase.service.covid19client;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Country(
    @JsonProperty("Country") String country,
    @JsonProperty("Slug") String slug) {

}
