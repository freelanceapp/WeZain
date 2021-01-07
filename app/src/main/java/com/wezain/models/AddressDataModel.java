package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class AddressDataModel implements Serializable {
    private List<AddressModel> data;
    private int status;

    public List<AddressModel> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
