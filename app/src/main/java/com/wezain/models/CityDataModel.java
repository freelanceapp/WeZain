package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class CityDataModel implements Serializable {
    private List<CityModel> data;

    public List<CityModel> getData() {
        return data;
    }
}
