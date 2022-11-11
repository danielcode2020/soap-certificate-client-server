package com.example.springwscertificateauthsoap.client;


import com.example.springwscertificateauthsoap.GeneratedXSD.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;

import java.io.IOException;

@Configuration
@Profile("client")
public class ClientConfig {
//    @Bean
//    public ObjectFactory objectFactory(){
//        return new ObjectFactory();
//    }

    @Bean
    public Wss4jSecurityInterceptor securityInterceptor(@Qualifier("cryptoFactoryBeanClient") CryptoFactoryBean cryptoFactoryBeanClient) throws Exception {
        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();

        // set security actions
        securityInterceptor.setSecurementActions("Timestamp Signature");

        // sign the request
        securityInterceptor.setSecurementUsername("client-app");
        securityInterceptor.setSecurementPassword("client-parola");
        securityInterceptor.setTimestampPrecisionInMilliseconds(true);
        securityInterceptor.setTimestampStrict(true);
        securityInterceptor.setSecurementSignatureCrypto(cryptoFactoryBeanClient.getObject());

        // encrypt the request
        securityInterceptor.setSecurementEncryptionUser("server-app");
        securityInterceptor.setSecurementEncryptionCrypto(cryptoFactoryBeanClient.getObject());
        securityInterceptor.setSecurementEncryptionParts("{Content}{http://spring.io/guides/gs-producing-web-service}getCountryResponse");
        securityInterceptor.setSecurementSignatureKeyIdentifier("DirectReference");
        securityInterceptor.setSecurementSignatureParts("{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body;\n" +
                "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp");
        securityInterceptor.setValidationSignatureCrypto(cryptoFactoryBeanClient.getObject());

        securityInterceptor.setValidateResponse(false);
        securityInterceptor.setSecureResponse(true);
        securityInterceptor.setValidationActions("Timestamp Signature");

        return securityInterceptor;
    }

    @Bean(name="cryptoFactoryBeanClient")
    public CryptoFactoryBean getCryptoFactoryBean() throws IOException {
        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStorePassword("client-parola");
        cryptoFactoryBean.setKeyStoreLocation(new ClassPathResource("keystore/client-app.p12"));
        return cryptoFactoryBean;
    }


    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setPackagesToScan("com.example.springwscertificateauthsoap.GeneratedXSD");
        return marshaller;
    }


    @Bean
    public WebServiceTemplate webServiceTemplate() throws Exception{
        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        ClientInterceptor[] interceptors = new ClientInterceptor[]{securityInterceptor(getCryptoFactoryBean())};
        webServiceTemplate.setInterceptors(interceptors);
        webServiceTemplate.afterPropertiesSet();
        return webServiceTemplate;
    }


}
