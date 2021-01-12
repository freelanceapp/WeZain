package com.wezain.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public static class User implements Serializable {
        private int id;
        private String code;
        private String user_type;
        private String vendor_id;
        private String first_name;
        private String last_name;
        private String email;
        private String phone_code;
        private String phone;
        private String api_token;
        private String google_id;
        private String facebook_id;
        private String twitter_id;
        private String using_social;
        private String address;
        private String latitude;
        private String longitude;
        private String logo;
        private String banner;
        private String email_verified_at;
        private String is_blocked;
        private String is_login;
        private String logout_time;
        private String is_confirmed;
        private String confirmation_code;
        private String forget_password_code;
        private String software_type;
        private String added_by;
        private String country_id;
        private String state;
        private String postcode;
        private String balance;
        private String balance_currency;
        private String deleted_at;
        private String created_at;
        private String updated_at;
        private String token;
        private String fireBaseToken;

        public User() {
        }


        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getUser_type() {
            return user_type;
        }

        public String getVendor_id() {
            return vendor_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public String getPhone() {
            return phone;
        }

        public String getApi_token() {
            return api_token;
        }

        public String getGoogle_id() {
            return google_id;
        }

        public String getFacebook_id() {
            return facebook_id;
        }

        public String getTwitter_id() {
            return twitter_id;
        }

        public String getUsing_social() {
            return using_social;
        }

        public String getAddress() {
            return address;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLogo() {
            return logo;
        }

        public String getBanner() {
            return banner;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public String getIs_blocked() {
            return is_blocked;
        }

        public String getIs_login() {
            return is_login;
        }

        public String getLogout_time() {
            return logout_time;
        }

        public String getIs_confirmed() {
            return is_confirmed;
        }

        public String getConfirmation_code() {
            return confirmation_code;
        }

        public String getForget_password_code() {
            return forget_password_code;
        }

        public String getSoftware_type() {
            return software_type;
        }

        public String getAdded_by() {
            return added_by;
        }

        public String getCountry_id() {
            return country_id;
        }

        public String getState() {
            return state;
        }

        public String getPostcode() {
            return postcode;
        }

        public String getBalance() {
            return balance;
        }

        public String getBalance_currency() {
            return balance_currency;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getToken() {
            return token;
        }

        public String getFireBaseToken() {
            return fireBaseToken;
        }

        public void setFireBaseToken(String fireBaseToken) {
            this.fireBaseToken = fireBaseToken;
        }


    }
}
