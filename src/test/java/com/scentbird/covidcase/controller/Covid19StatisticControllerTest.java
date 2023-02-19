package com.scentbird.covidcase.controller;

import com.scentbird.covidcase.AbstractTest;
import com.scentbird.covidcase.service.covid19client.Country;
import com.scentbird.covidcase.service.covid19client.Covid19Case;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Covid19StatisticControllerTest extends AbstractTest {

    private static final String URL = "/api/v1/covid19-statistics";

    @Test
    public void shouldGetConfirmedStatistics() throws Exception {
        // given
        var from = LocalDate.parse("2022-01-01");
        var to = LocalDate.parse("2022-01-02");
        when(mockCovid19Client.getCountries()).thenReturn(List.of(
            new Country("Portugal", "portugal"),
            new Country("Norway", "norway")
        ));
        when(mockCovid19Client.getConfirmedCases(
            eq("portugal"),
            eq(from.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)),
            eq(to.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)))
        ).thenReturn(List.of(
            new Covid19Case(
                "Portugal",
                1000L,
                from.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            new Covid19Case(
                "Portugal",
                1005L,
                from.atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            new Covid19Case(
                "Portugal",
                1015L,
                to.atStartOfDay().toInstant(ZoneOffset.UTC)
            )
        ));
        when(mockCovid19Client.getConfirmedCases(
            eq("norway"),
            eq(from.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)),
            eq(to.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)))
        ).thenReturn(List.of(
            new Covid19Case(
                "Norway",
                500L,
                from.minusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            new Covid19Case(
                "Norway",
                501L,
                from.atStartOfDay().toInstant(ZoneOffset.UTC)
            ),
            new Covid19Case(
                "Norway",
                503L,
                to.atStartOfDay().toInstant(ZoneOffset.UTC)
            )
        ));

        // when
        var result = mockMvc.perform(
            get(URL + "/confirmed?aggregationTypes=MIN,MAX&countries=Norway,Portugal&from=2022-01-01&to=2022-01-02")
        );

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].aggregationType", is("MIN")))
            .andExpect(jsonPath("$.[0].country", is("Norway")))
            .andExpect(jsonPath("$.[0].date", is("2022-01-01")))
            .andExpect(jsonPath("$.[0].cases", equalTo(1)))
            .andExpect(jsonPath("$.[1].aggregationType", is("MAX")))
            .andExpect(jsonPath("$.[1].country", is("Portugal")))
            .andExpect(jsonPath("$.[1].date", is("2022-01-02")))
            .andExpect(jsonPath("$.[1].cases", equalTo(10)));
    }

    @Test
    public void shouldReturnBadRequestWhenCountryIsNotFound() throws Exception {
        when(mockCovid19Client.getCountries()).thenReturn(List.of(
            new Country("Portugal", "portugal"),
            new Country("Norway", "norway")
        ));

        mockMvc.perform(
                get(URL + "/confirmed?aggregationTypes=MIN,MAX&countries=Invalid&from=2022-01-01&to=2022-01-02")
            )
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andExpect(result -> assertEquals("400 BAD_REQUEST \"The next countries not found: [Invalid]\"",
                result.getResolvedException().getMessage()));
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidTimeInterval() throws Exception {
        mockMvc.perform(
                get(URL + "/confirmed?aggregationTypes=MIN,MAX&countries=Portugal&from=2022-01-02&to=2022-01-01")
            )
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
            .andExpect(result -> assertEquals("400 BAD_REQUEST \"Invalid time interval.\"",
                result.getResolvedException().getMessage()));
    }
}
