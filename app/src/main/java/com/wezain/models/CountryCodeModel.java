package com.wezain.models;

import java.io.Serializable;

public class CountryCodeModel implements Serializable {
    private String  id;
    private String code;
    private String name;
    private String country;

    public CountryCodeModel(String id, String code, String name, String country) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }
}
