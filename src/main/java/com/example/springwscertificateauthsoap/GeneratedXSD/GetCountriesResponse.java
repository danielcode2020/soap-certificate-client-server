package com.example.springwscertificateauthsoap.GeneratedXSD;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "getCountriesResponse")
public class GetCountriesResponse {

    @XmlElementWrapper
    @XmlElement(required = true)
    protected List<Country> countries;

    /**
     * Gets the value of the country property.
     *
     * @return
     *     possible object is
     *     {@link Country }
     *
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * Sets the value of the country property.
     *
     * @param value
     *     allowed object is
     *     {@link Country }
     *
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
