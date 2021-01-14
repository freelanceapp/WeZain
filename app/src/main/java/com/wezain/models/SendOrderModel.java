package com.wezain.models;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.wezain.BR;
import com.wezain.R;

import java.io.Serializable;

public class SendOrderModel extends BaseObservable implements Serializable {
    private String first_name;
    private String last_name;
    private String phone_code;
    private String phone;
    private CountryCodeModel countryModel;
    private CityModel cityModel;
    private String state;
    private String address;


    public ObservableField<String> error_first_name = new ObservableField<>();
    public ObservableField<String> error_last_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_state = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();


    public SendOrderModel() {
        first_name = "";
        last_name="";
        phone="";
        countryModel=null;
        cityModel=null;
        state="";
        address="";
    }

    public boolean isDataValid(Context context){
        if (!first_name.isEmpty()&&
                !last_name.isEmpty()&&
                !phone.isEmpty()&&
                !state.isEmpty()&&
                !address.isEmpty()&&
                countryModel!=null&&
                cityModel!=null

        ){
            error_first_name.set(null);
            error_last_name.set(null);
            error_phone.set(null);
            error_state.set(null);
            error_address.set(null);

            return true;
        }else {

            if (first_name.isEmpty()){
                error_first_name.set(context.getString(R.string.field_req));
            }else {
                error_first_name.set(null);

            }

            if (last_name.isEmpty()){
                error_last_name.set(context.getString(R.string.field_req));
            }else {
                error_last_name.set(null);

            }

            if (phone.isEmpty()){
                error_phone.set(context.getString(R.string.field_req));
            }else {
                error_phone.set(null);

            }

            if (countryModel==null){
                Toast.makeText(context, R.string.ch_country, Toast.LENGTH_SHORT).show();
            }

            if (cityModel==null){
                Toast.makeText(context, R.string.ch_city, Toast.LENGTH_SHORT).show();
            }

            if (state.isEmpty()){
                error_state.set(context.getString(R.string.field_req));
            }else {
                error_state.set(null);

            }

            if (address.isEmpty()){
                error_address.set(context.getString(R.string.field_req));
            }else {
                error_address.set(null);

            }




            return false;
        }
    }

    @Bindable
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
        notifyPropertyChanged(BR.first_name);

    }

    @Bindable
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
        notifyPropertyChanged(BR.last_name);

    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    public CountryCodeModel getCountryModel() {
        return countryModel;
    }

    public void setCountryModel(CountryCodeModel countryModel) {
        this.countryModel = countryModel;
    }

    public CityModel getCityModel() {
        return cityModel;
    }

    public void setCityModel(CityModel cityModel) {
        this.cityModel = cityModel;
    }

    @Bindable
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        notifyPropertyChanged(BR.state);

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }
}
