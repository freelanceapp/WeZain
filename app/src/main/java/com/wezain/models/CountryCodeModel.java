package com.wezain.models;

import java.io.Serializable;

public class CountryCodeModel implements Serializable {
    private String code;
    private String name;

    public CountryCodeModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
