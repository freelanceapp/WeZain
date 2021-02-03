package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {
    private int id;
    private String user_id;
    private String driver_id;
    private String first_name;
    private String last_name;
    private String address;
    private String country_id;
    private String phone;
    private String state;
    private String city;
    private String status;
    private String shipping_cost;
    private String total_cost;
    private String bill_currency;
    private String longitude;
    private String latitude;
    private String other_phone;
    private String date;
    private String time;
    private String payment_type;
    private String shipping_method;
    private String start_shipping_date;
    private String start_shipping_time;
    private String end_shipping_date;
    private String end_shipping_time;
    private String rating;
    private String payPal_payment_id;
    private String payerID;
    private String payPal_token;
    private String payPal_payment_status;
    private String created_at;
    private String updated_at;
    private List<OrderDetailsProduct> order_details_product;


    public static class ProductDetails implements Serializable {
        private int id;
        private String title;
        private String desc;
        private String price_type;
        private String price;
        private String old_price;
        private String main_image;
        private String have_offer;
        private String offer_type;
        private String offer_value;
        private String stock;
        private String stock_amount;
        private String upcoming_time;
        private String view_counts;
        private List<ProductImage> product_images;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getPrice_type() {
            return price_type;
        }

        public String getPrice() {
            return price;
        }

        public String getOld_price() {
            return old_price;
        }

        public String getMain_image() {
            return main_image;
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

        public String getStock() {
            return stock;
        }

        public String getStock_amount() {
            return stock_amount;
        }

        public String getUpcoming_time() {
            return upcoming_time;
        }

        public String getView_counts() {
            return view_counts;
        }

        public List<ProductImage> getProduct_images() {
            return product_images;
        }
    }

    public static class OrderDetailsProduct implements Serializable {
        private int id;
        private String amount;
        private String total_price;
        private String have_offer;
        private String offer_value;
        private String single_price_be_offer;
        private String created_at;
        private ProductDetails product_details;


        public int getId() {
            return id;
        }

        public String getAmount() {
            return amount;
        }

        public String getTotal_price() {
            return total_price;
        }

        public String getHave_offer() {
            return have_offer;
        }

        public String getOffer_value() {
            return offer_value;
        }

        public String getSingle_price_be_offer() {
            return single_price_be_offer;
        }

        public String getCreated_at() {
            return created_at;
        }

        public ProductDetails getProduct_details() {
            return product_details;
        }

    }

    public static class ProductImage implements Serializable {
        private int id;
        private String name;
        private String product_id;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }


    public int getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStatus() {
        return status;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public String getBill_currency() {
        return bill_currency;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getOther_phone() {
        return other_phone;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getShipping_method() {
        return shipping_method;
    }

    public String getStart_shipping_date() {
        return start_shipping_date;
    }

    public String getStart_shipping_time() {
        return start_shipping_time;
    }

    public String getEnd_shipping_date() {
        return end_shipping_date;
    }

    public String getEnd_shipping_time() {
        return end_shipping_time;
    }

    public String getRating() {
        return rating;
    }

    public String getPayPal_payment_id() {
        return payPal_payment_id;
    }

    public String getPayerID() {
        return payerID;
    }

    public String getPayPal_token() {
        return payPal_token;
    }

    public String getPayPal_payment_status() {
        return payPal_payment_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public List<OrderDetailsProduct> getOrder_details_product() {
        return order_details_product;
    }
}
