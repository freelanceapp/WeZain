package com.wezain.models;

import java.io.Serializable;

public class SingleOrderModel implements Serializable {
    private OrderModel data;

    public OrderModel getData() {
        return data;
    }
}
