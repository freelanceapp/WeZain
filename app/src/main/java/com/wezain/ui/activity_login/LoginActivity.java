package com.wezain.ui.activity_login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wezain.R;
import com.wezain.adapters.CountriesAdapter;
import com.wezain.databinding.ActivityLoginBinding;
import com.wezain.databinding.DialogCountriesBinding;
import com.wezain.language.Language;
import com.wezain.models.CountryCodeModel;
import com.wezain.models.LoginModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.share.Common;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_sign_up.SignUpActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }



    private void initView() {
        model = new LoginModel();
        binding.tvSkip.setPaintFlags(binding.tvSkip.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        binding.setModel(model);

        binding.btnLogin.setOnClickListener(view -> {
            if (model.isDataValid(this)){
                login();
            }
        });

        binding.tvSkip.setOnClickListener(view -> {
            navigateToHomeActivity();
        });

        binding.tvSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this,SignUpActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void login()
    {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .login(model.getEmail(),model.getPassword())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                preferences = Preferences.getInstance();
                                preferences.create_update_userdata(LoginActivity.this,response.body());
                                navigateToHomeActivity();
                            }


                        } else {

                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if (response.code()==401){
                                Toast.makeText(LoginActivity.this, R.string.user_not_exist, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(LoginActivity.this,getString(R.string.something) , Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this,getString(R.string.failed) , Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}