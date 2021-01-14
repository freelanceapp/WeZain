package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private List<OrderModel> data;

    public List<OrderModel> getData() {
        return data;
    }
}
