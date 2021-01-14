package com.wezain.models;

import java.io.Serializable;

public class CityModel implements Serializable {
    private int id;
    private String city_name;
    private String city_name_en;

    public CityModel(int id, String city_name, String city_name_en) {
        this.id = id;
        this.city_name = city_name;
        this.city_name_en = city_name_en;
    }

    public int getId() {
        return id;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getCity_name_en() {
        return city_name_en;
    }
}
