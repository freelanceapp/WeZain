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

public class ActivitySignUpPresenter implements DatePickerDialog.OnDateSetListener{
    private Context context;
    private ActivitySignUpView view;

    private DatePickerDialog datePickerDialog;



    public ActivitySignUpPresenter(Context context, ActivitySignUpView view)
    {
        this.context = context;
        this.view = view;

        createDateDialog();

    }

    private void createDateDialog()
    {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.MONTH,Calendar.JANUARY);
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOkText(context.getString(R.string.select));
        datePickerDialog.setCancelText(context.getString(R.string.cancel));
        datePickerDialog.setAccentColor(ContextCompat.getColor(context, R.color.colorPrimary));
        datePickerDialog.setOkColor(ContextCompat.getColor(context, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ContextCompat.getColor(context, R.color.gray4));
        datePickerDialog.setLocale(Locale.ENGLISH);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setMaxDate(calendar);

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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        ActivitySignUpPresenter.this.view.onDateSelected(date);
    }
}
