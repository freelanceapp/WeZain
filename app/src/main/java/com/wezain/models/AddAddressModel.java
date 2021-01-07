package com.wezain.models;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.wezain.BR;
import com.wezain.R;

import java.io.Serializable;

public class AddAddressModel extends BaseObservable implements Serializable {
    private String address_id;
    private String user_id;
    private String user_name;
    private String phone;
    private String address;
    private double lat;
    private double lng;
    private String additionalNote;
    private String type;
    public ObservableField<String> error_phone = new ObservableField<>();


    public boolean isDataValid(Context context){
        if (!phone.isEmpty()){
            error_phone.set(null);
            return true;
        }else {
            error_phone.set(context.getString(R.string.field_req));
            return false;
        }
    }

    public AddAddressModel() {
        this.address_id ="";
        this.user_id ="";
        this.user_name = "";
        this.phone = "";
        this.address="";
        this.lat=0.0;
        this.lng=0.0;
        this.additionalNote ="";
        this.type ="home";
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Bindable
    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
        notifyPropertyChanged(BR.additionalNote);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }
}
