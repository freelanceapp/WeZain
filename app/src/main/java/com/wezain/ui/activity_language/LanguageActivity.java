package com.wezain.ui.activity_language;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.wezain.R;
import com.wezain.databinding.ActivityLanguageBinding;
import com.wezain.databinding.DialogAlertBinding;
import com.wezain.databinding.DialogCartBinding;
import com.wezain.language.Language;
import com.wezain.models.CartDataModel;
import com.wezain.preferences.Preferences;

import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {
    private ActivityLanguageBinding binding;
    private String lang = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");

        if (lang.equals("ar")){
            binding.flAr.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.flEn.setBackgroundResource(0);
        }else {
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_rounded_red_strock);
        }
        binding.cardAr.setOnClickListener(view -> {
            lang = "ar";
            binding.flAr.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.flEn.setBackgroundResource(0);
            binding.btnNext.setVisibility(View.VISIBLE);

        });

        binding.cardEn.setOnClickListener(view -> {
            lang = "en";
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_rounded_red_strock);
            binding.btnNext.setVisibility(View.VISIBLE);
        });

        binding.btnNext.setVisibility(View.VISIBLE);

        binding.btnNext.setOnClickListener(view -> {

            Intent intent = getIntent();
            intent.putExtra("lang", lang);
            setResult(RESULT_OK, intent);
            finish();

            /*Preferences preferences = Preferences.getInstance();
            CartDataModel cartDataModel = preferences.getCartData(this);
            if (cartDataModel==null){

            }else {
                if (cartDataModel.getProducts()!=null&&cartDataModel.getProducts().size()>0){
                    createDialogAlert();

                }else {
                    Intent intent = getIntent();
                    intent.putExtra("lang", lang);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }*/
        });
    }


    private void createDialogAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogCartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_cart, null, false);

        binding.tvMsg.setText(R.string.will_delete_cart);
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()
        );

        binding.btnDelete.setOnClickListener(v -> {
                    Preferences preferences = Preferences.getInstance();
                    preferences.clearCart(this);
                    Intent intent = getIntent();
                    intent.putExtra("lang", lang);
                    setResult(RESULT_OK, intent);
                    onBackPressed();
                    dialog.dismiss();

                }
        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
}