package com.scentbird.covidcase.service.aggregation;

import com.scentbird.covidcase.dto.AggregationType;
import com.scentbird.covidcase.service.covid19client.CacheableCovid19Client;
import org.springframework.stereotype.Service;

@Service
public class MinConfirmedCasesAggregation extends AbstractAggregation {

    public MinConfirmedCasesAggregation(CacheableCovid19Client covid19Client) {
        super(covid19Client, AggregationType.MIN, (newCases, minCases) -> newCases < minCases);
    }

}
