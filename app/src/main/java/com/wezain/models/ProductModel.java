package com.wezain.models;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private int id;
    private String title;
    private String main_image;
    private String price;
    private String old_price;
    private String have_offer;
    private String offer_type;
    private String offer_value;
    private ProductLikes product_likes;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMain_image() {
        return main_image;
    }

    public String getPrice() {
        return price;
    }

    public String getOld_price() {
        return old_price;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public String getOffer_value() {
        return offer_value;
    }

    public ProductLikes getProduct_likes() {
        return product_likes;
    }

    public void setProduct_likes(ProductLikes product_likes) {
        this.product_likes = product_likes;
    }

    public static class ProductLikes implements Serializable{

    }
}
