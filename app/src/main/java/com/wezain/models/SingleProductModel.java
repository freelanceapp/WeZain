package com.wezain.models;

import java.io.Serializable;

public class SingleProductModel implements Serializable {
    private ProductModel data;

    public ProductModel getData() {
        return data;
    }
}
