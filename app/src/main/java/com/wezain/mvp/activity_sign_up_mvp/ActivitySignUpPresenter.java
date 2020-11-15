package com.wezain.mvp.activity_sign_up_mvp;

import android.app.FragmentManager;
import android.content.Context;

import androidx.core.content.ContextCompat;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wezain.R;
import com.wezain.models.SignUpModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivitySignUpPresenter {
    private Context context;
    private ActivitySignUpView view;

    private DatePickerDialog datePickerDialog;



    public ActivitySignUpPresenter(Context context, ActivitySignUpView view)
    {
        this.context = context;
        this.view = view;


    }




    public void checkData(SignUpModel signUpModel)
    {
        if (signUpModel.isDataValid(context)){
            if (signUpModel.getImageUrl().isEmpty()){
                sign_up_without_image(signUpModel);
            }else {
                sign_up_with_image(signUpModel);

            }
        }
    }
    public void showDateDialog(FragmentManager fragmentManager){
        try {
            datePickerDialog.show(fragmentManager,"");

        }catch (Exception e){}
    }

    private void sign_up_with_image(SignUpModel signUpModel) {

    }

    private void sign_up_without_image(SignUpModel signUpModel) {


    }


}
