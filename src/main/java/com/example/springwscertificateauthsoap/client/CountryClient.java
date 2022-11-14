package com.example.springwscertificateauthsoap.client;


import com.example.springwscertificateauthsoap.GeneratedXSD.GetCountryRequest;
import com.example.springwscertificateauthsoap.GeneratedXSD.GetCountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Component
@Profile("client")
public class CountryClient{

    private static final Logger log = LoggerFactory.getLogger(CountryClient.class);

    private final ClientConfig clientConfig;

    public CountryClient(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public GetCountryResponse getCountry(String country) throws Exception {
        GetCountryRequest request = new GetCountryRequest();
        request.setName(country);

        log.info("Requesting location for " + country);

        GetCountryResponse response = (GetCountryResponse) clientConfig.webServiceTemplate()
                .marshalSendAndReceive("http://localhost:8090/ws/countries", request,
                        new SoapActionCallback(
                                "http://spring.io/guides/gs-producing-web-service/GetCountryRequest"));
        return response;
    }



}
