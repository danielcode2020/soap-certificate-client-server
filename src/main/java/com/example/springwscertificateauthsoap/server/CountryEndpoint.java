package com.example.springwscertificateauthsoap.server;

import com.example.springwscertificateauthsoap.GeneratedXSD.GetCountriesResponse;
import com.example.springwscertificateauthsoap.GeneratedXSD.GetCountryRequest;
import com.example.springwscertificateauthsoap.GeneratedXSD.GetCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Profile("server")
@Endpoint
public class CountryEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private CountryRepository countryRepository;

    @Autowired
    public CountryEndpoint(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(countryRepository.findCountry(request.getName()));

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountriesRequest")
    @ResponsePayload
    public GetCountriesResponse getCountriesResponse(){
        GetCountriesResponse response = new GetCountriesResponse();
        response.setCountries(countryRepository.getCountries());
        return response;
    }
}
