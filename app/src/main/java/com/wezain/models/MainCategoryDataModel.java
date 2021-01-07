package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class MainCategoryDataModel implements Serializable {
    private List<CategoryDataModel> data;

    public List<CategoryDataModel> getData() {
        return data;
    }
}

