package com.scentbird.covidcase.controller;

import com.scentbird.covidcase.AbstractTest;
import com.scentbird.covidcase.service.covid19client.Country;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CountryControllerTest extends AbstractTest {

    private static final String URL = "/api/v1/countries";

    @Test
    public void shouldGetCountries() throws Exception {
        // given
        when(mockCovid19Client.getCountries()).thenReturn(List.of(
            new Country("Portugal", "portugal"),
            new Country("Norway", "norway")
        ));

        // when
        var result = mockMvc.perform(get(URL));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0]", is("Portugal")))
            .andExpect(jsonPath("$.[1]", is("Norway")));
    }

}
