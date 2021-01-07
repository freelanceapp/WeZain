package com.wezain.models;

import java.io.Serializable;

public class SliderModel implements Serializable {
    private int id;
    private String product_id;
    private String title;
    private String desc;
    private String main_image;
    private String offer_type;

    public int getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getOffer_type() {
        return offer_type;
    }
}
