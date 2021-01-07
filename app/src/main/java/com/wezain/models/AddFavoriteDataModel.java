package com.wezain.models;

import java.io.Serializable;

public class AddFavoriteDataModel implements Serializable {
    private ProductModel.ProductLikes data;

    public ProductModel.ProductLikes getData() {
        return data;
    }
}
