package com.wezain.ui.activity_login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wezain.R;
import com.wezain.databinding.ActivityLoginBinding;
import com.wezain.language.Language;
import com.wezain.models.LoginModel;
import com.wezain.mvp.activity_login_presenter.ActivityLoginPresenter;
import com.wezain.mvp.activity_login_presenter.ActivityLoginView;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity implements ActivityLoginView {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private ActivityLoginPresenter presenter;
    private double lat=0.0,lng=0.0;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);
    }

    private void initView() {
        model = new LoginModel();
      //  binding.tv1.setText(Html.fromHtml(getString(R.string.login2)));
        binding.tvSkip.setPaintFlags(binding.tvSkip.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        binding.setModel(model);
        presenter = new ActivityLoginPresenter(this,this);
        binding.btnLogin.setOnClickListener(view -> {
            presenter.checkData(model);
        });

        binding.tvSkip.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("lat",lat);
            intent.putExtra("lng",lng);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onLoginValid() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone_code",model.getPhone_code());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        startActivity(intent);
        finish();

    }
}