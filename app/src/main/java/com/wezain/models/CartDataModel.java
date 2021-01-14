package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class CartDataModel implements Serializable {
    private String user_id;
    private String first_name;
    private String last_name;
    private double total_cost=0.0;
    private String payment_type;
    private String address;
    private String state;
    private int country_id;
    private String city;
    private String phone;
    private String bill_currency;
    private String lang;
    private List<CartModel> products;

    public CartDataModel() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static class CartModel implements Serializable{
        private String price_id;
        private String image;
        private String name;
        private int amount;
        private double price;

        public CartModel(String price_id, String image, String name, int amount, double price) {
            this.price_id = price_id;
            this.image = image;
            this.name = name;
            this.amount = amount;
            this.price = price;
        }

        public String getPrice_id() {
            return price_id;
        }

        public void setPrice_id(String price_id) {
            this.price_id = price_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public List<CartModel> getProducts() {
        return products;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setProducts(List<CartModel> products) {
        this.products = products;
    }

    public String getState() {
        return state;
    }

    public int getCountry_id() {
        return country_id;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBill_currency() {
        return bill_currency;
    }

    public void setBill_currency(String bill_currency) {
        this.bill_currency = bill_currency;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }
}
