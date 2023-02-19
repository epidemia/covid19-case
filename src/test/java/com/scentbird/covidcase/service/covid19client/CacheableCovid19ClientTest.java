package com.scentbird.covidcase.service.covid19client;

import com.scentbird.covidcase.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CacheableCovid19ClientTest extends AbstractTest {

    @Autowired
    private CacheableCovid19Client cacheableCovid19Client;

    @Test
    public void shouldCheckThatGetCountriesMethodIsCacheable() {
        when(mockCovid19Client.getCountries()).thenReturn(Collections.emptyList());

        cacheableCovid19Client.getCountries();
        cacheableCovid19Client.getCountries();

        verify(mockCovid19Client, atMostOnce()).getCountries();
    }

    @Test
    public void shouldCheckThatGetConfirmedCasesMethodIsCacheable() {
        var country = "Finland";
        var from = Instant.now();
        var to = Instant.now();
        when(mockCovid19Client.getConfirmedCases(eq(country), eq(from), eq(to))).thenReturn(Collections.emptyList());

        cacheableCovid19Client.getConfirmedCases(country, from, to);
        cacheableCovid19Client.getConfirmedCases(country, from, to);

        verify(mockCovid19Client, times(1)).getConfirmedCases(eq(country), eq(from), eq(to));
    }

}
