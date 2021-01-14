package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {
    private int id;
    private String title;
    private String desc;
    private String main_image;
    private String price;
    private String old_price;
    private String have_offer;
    private String offer_type;
    private String offer_value;
    private String view_counts;
    private String price_type;
    private int rate_count;
    private int rate_avg;
    private ProductLikes product_likes;
    private List<Product_Images> product_images;
    private List<Product_Prices> product_prices;

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

    public String getDesc() {
        return desc;
    }

    public String getView_counts() {
        return view_counts;
    }

    public int getRate_count() {
        return rate_count;
    }

    public int getRate_avg() {
        return rate_avg;
    }

    public List<Product_Images> getProduct_images() {
        return product_images;
    }

    public List<Product_Prices> getProduct_prices() {
        return product_prices;
    }

    public String getPrice_type() {
        return price_type;
    }

    public static class ProductLikes implements Serializable{

    }

    public static class Product_Images implements Serializable{
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class Product_Prices implements Serializable{
        private int id;
        private String option1;
        private String value1;
        private String option2;
        private String value2;
        private String price;
        private boolean isSelected=false;

        public int getId() {
            return id;
        }

        public String getOption1() {
            return option1;
        }

        public String getValue1() {
            return value1;
        }

        public String getOption2() {
            return option2;
        }

        public String getValue2() {
            return value2;
        }

        public String getPrice() {
            return price;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
