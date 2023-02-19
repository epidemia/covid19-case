package com.scentbird.covidcase.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import static org.springdoc.core.Constants.SWAGGER_CONFIG_URL;
import static org.springdoc.core.Constants.SWAGGER_UI_URL;

@RestController
public class SwaggerController {

    @Value(SWAGGER_CONFIG_URL)
    private String configUrl;

    @Hidden
    @RequestMapping(value = "/swagger", method = RequestMethod.GET)
    public void getSwagger(HttpServletResponse httpServletResponse) {
        var url = String.format("%s?configUrl=%s", SWAGGER_UI_URL, configUrl);
        httpServletResponse.setHeader(HttpHeaders.LOCATION, url);
        httpServletResponse.setStatus(HttpStatus.SEE_OTHER.value());
    }

}
