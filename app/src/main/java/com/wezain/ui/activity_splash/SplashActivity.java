package com.wezain.ui.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.wezain.R;
import com.wezain.databinding.ActivitySplashBinding;
import com.wezain.language.Language;
import com.wezain.models.UserModel;
import com.wezain.models.UserSettingsModel;

import com.wezain.preferences.Preferences;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_language.LanguageActivity;
import com.wezain.ui.activity_login.LoginActivity;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity{
    private ActivitySplashBinding binding;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Transition transition = new TransitionSet();
            transition.setInterpolator(new LinearInterpolator());
            transition.setDuration(500);
            getWindow().setEnterTransition(transition);
            getWindow().setExitTransition(transition);

        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        new Handler().postDelayed(() -> {
            Intent intent;
            if (userModel==null){
                intent = new Intent(this, LoginActivity.class);
            }else {
                intent = new Intent(this, HomeActivity.class);
            }
            startActivity(intent);
            finish();
        },2000);
    }








}