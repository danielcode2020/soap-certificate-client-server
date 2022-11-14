package com.example.springwscertificateauthsoap.controller;

import com.example.springwscertificateauthsoap.GeneratedXSD.GetCountriesResponse;
import com.example.springwscertificateauthsoap.GeneratedXSD.GetCountryResponse;
import com.example.springwscertificateauthsoap.client.CountryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@Profile("client")
@RestController
@RequestMapping("/api/country")
public class TestController {

    private final CountryClient countryClient;

    public TestController(CountryClient countryClient) {
        this.countryClient = countryClient;
    }

    @GetMapping("/{name}")
    public GetCountryResponse getCountryByName(@PathVariable String name) throws Exception {
        System.out.println(name);
        return countryClient.getCountry(name);
    }

    @GetMapping
    public GetCountriesResponse getCountries() throws Exception {
        return countryClient.getCountries();
    }
}
