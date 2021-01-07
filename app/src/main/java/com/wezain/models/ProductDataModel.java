package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class ProductDataModel implements Serializable {
    private List<ProductModel> data;

    public List<ProductModel> getData() {
        return data;
    }
}
