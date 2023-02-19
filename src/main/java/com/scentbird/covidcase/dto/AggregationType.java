package com.scentbird.covidcase.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Aggregation Type")
public enum AggregationType {

    MIN,
    MAX

}
