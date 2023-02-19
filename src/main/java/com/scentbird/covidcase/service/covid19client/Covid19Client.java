package com.scentbird.covidcase.service.covid19client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.List;

@FeignClient(
    name = "covid19-client",
    url = "${feign.client.config.covid19-api.url}",
    primary = false,
    qualifiers = "covid19Client"
)
public interface Covid19Client {

    @GetMapping("/countries")
    List<Country> getCountries();

    @GetMapping("/country/{country}/status/confirmed?from={from}&to={to}")
    List<Covid19Case> getConfirmedCases(
        @PathVariable String country,
        @PathVariable Instant from,
        @PathVariable Instant to
    );

}
