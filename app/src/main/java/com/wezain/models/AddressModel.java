package com.wezain.models;

import java.io.Serializable;

public class AddressModel implements Serializable {
    private int id;
    private String user_id;
    private String name;
    private String phone;
    private String other_phone;
    private String address;
    private String city;
    private String google_lat;
    private String google_long;
    private String type;
    private String updated_at;
    private String created_at;


    public int getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getOther_phone() {
        return other_phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getGoogle_lat() {
        return google_lat;
    }

    public String getGoogle_long() {
        return google_long;
    }

    public String getType() {
        return type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }
}
