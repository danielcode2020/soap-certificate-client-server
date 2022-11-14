package com.example.springwscertificateauthsoap.server;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.io.IOException;
import java.util.List;

@EnableWs
@Configuration
@Profile("server")
public class ServerConfig extends WsConfigurerAdapter {


    @Bean
    public KeyStoreCallbackHandler securityCallbackHandler(){
        KeyStoreCallbackHandler callbackHandler = new KeyStoreCallbackHandler();
        callbackHandler.setPrivateKeyPassword("server-parola");
        return callbackHandler;
    }

    @Bean
    public Wss4jSecurityInterceptor securityInterceptor() throws Exception{
        Wss4jSecurityInterceptor
                securityInterceptor = new Wss4jSecurityInterceptor();

        //validate incoming request
        securityInterceptor.setValidationActions("Signature Timestamp");
        securityInterceptor.setValidationSignatureCrypto(getCryptoFactoryBean().getObject());
        securityInterceptor.setValidationDecryptionCrypto(getCryptoFactoryBean().getObject());
        securityInterceptor.setValidationCallbackHandler(securityCallbackHandler());
        securityInterceptor.setTimestampStrict(true);

        //encrypt the response
        securityInterceptor.setSecurementEncryptionUser("client-app");
        securityInterceptor.setSecurementEncryptionParts("{Content}{http://spring.io/guides/gs-producing-web-service}getAllCountriesResponse");
        securityInterceptor.setSecurementEncryptionParts("{Content}{http://spring.io/guides/gs-producing-web-service}getCountryResponse");
        securityInterceptor.setSecurementSignatureParts("{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body;\n" +
                "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp");
        securityInterceptor.setSecurementEncryptionCrypto(getCryptoFactoryBean().getObject());

        //sign the response
        securityInterceptor.setSecurementActions("Signature Timestamp");
        securityInterceptor.setSecurementUsername("server-app");
        securityInterceptor.setSecurementPassword("server-parola");
        securityInterceptor.setSecurementSignatureCrypto(getCryptoFactoryBean().getObject());
        securityInterceptor.setSecureResponse(true);
        securityInterceptor.setEnableSignatureConfirmation(false);
        securityInterceptor.setValidateResponse(true);

        return securityInterceptor;
    }

    @Bean
    public CryptoFactoryBean getCryptoFactoryBean() throws IOException {

        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
        cryptoFactoryBean.setKeyStorePassword("server-parola");
        cryptoFactoryBean.setKeyStoreLocation( new ClassPathResource("keystore/server-app.p12"));
        return cryptoFactoryBean;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors){
        try{
            interceptors.add(securityInterceptor());
        }catch (Exception e){
            throw new RuntimeException("Could not initialize security interceptor");
        }
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "countries")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CountriesPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
    }
}
