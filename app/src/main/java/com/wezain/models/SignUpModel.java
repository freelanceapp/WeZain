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
import java.util.ArrayList;
import java.util.List;

public class SignUpModel extends BaseObservable implements Serializable {
    private String imageUrl;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String phone_code;
    private String phone;

    public ObservableField<String> error_first_name = new ObservableField<>();
    public ObservableField<String> error_last_name = new ObservableField<>();
    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public SignUpModel() {
        this.phone_code ="+971";
        this.phone = "";
        this.imageUrl="";
        this.first_name = "";
        this.last_name = "";
        this.email = "";
        this.password="";


    }

    public boolean isDataValid(Context context){
        if (!first_name.isEmpty()&&
                !last_name.isEmpty()&&
                !email.isEmpty()&&
                !phone.isEmpty()&&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                password.length()>=6
        ){
            error_email.set(null);
            error_password.set(null);
            error_first_name.set(null);
            error_last_name.set(null);
            error_phone.set(null);

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

            if (email.isEmpty()){
                error_email.set(context.getString(R.string.field_req));
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                error_email.set(context.getString(R.string.inv_email));

            }else {
                error_email.set(null);

            }

            if (password.isEmpty()){
                error_password.set(context.getString(R.string.field_req));
            }else if (password.length()<6){
                error_password.set(context.getString(R.string.password_short));

            }else {
                error_password.set(null);

            }



            return false;
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);

    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }


}
