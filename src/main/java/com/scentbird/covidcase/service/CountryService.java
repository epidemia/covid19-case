package com.scentbird.covidcase.service;

import com.scentbird.covidcase.service.covid19client.CacheableCovid19Client;
import com.scentbird.covidcase.service.covid19client.Country;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CacheableCovid19Client covid19Client;

    public CountryService(CacheableCovid19Client covid19Client) {
        this.covid19Client = covid19Client;
    }

    public List<String> getCounties() {
        return covid19Client.getCountries().stream()
            .map(Country::country)
            .toList();
    }

    public Map<String, String> getSlugByCountry() {
        return covid19Client.getCountries().stream()
            .collect(Collectors.toMap(Country::country, Country::slug));
    }

}
