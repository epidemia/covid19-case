package com.scentbird.covidcase.service.covid19client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CacheableCovid19Client {

    private final Covid19Client covid19Client;

    public CacheableCovid19Client(Covid19Client covid19Client) {
        this.covid19Client = covid19Client;
    }

    @Cacheable(value = "countries")
    public List<Country> getCountries() {
        return covid19Client.getCountries();
    }

    @Cacheable(value = "confirmedCases")
    public List<Covid19Case> getConfirmedCases(String country, Instant from, Instant to) {
        return covid19Client.getConfirmedCases(country, from, to);
    }
}
