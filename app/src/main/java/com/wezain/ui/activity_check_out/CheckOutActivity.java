package com.wezain.ui.activity_check_out;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Toast;

import com.wezain.R;
import com.wezain.adapters.CitySpinnerAdapter;
import com.wezain.adapters.CountrySpinnerAdapter;
import com.wezain.databinding.ActivityCheckOutBinding;
import com.wezain.databinding.ActivityLanguageBinding;
import com.wezain.language.Language;
import com.wezain.models.CityDataModel;
import com.wezain.models.CityModel;
import com.wezain.models.CountryCodeModel;
import com.wezain.models.SendOrderModel;
import com.wezain.models.SingleProductModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.share.Common;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_product_details.ProductDetailsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {

    private ActivityCheckOutBinding binding;
    private String lang = "";
    private Preferences preferences;
    private UserModel userModel;
    private SendOrderModel model;
    private List<CityModel> cityModelList;
    private CitySpinnerAdapter citySpinnerAdapter;
    private String country, myCountry;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_out);
        initView();
    }

    private void initView() {
        Paper.init(this);
        cityModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        model = new SendOrderModel();
        model.setFirst_name(userModel.getData().getFirst_name());
        model.setLast_name(userModel.getData().getLast_name());
        model.setPhone_code(userModel.getData().getPhone_code());
        model.setPhone(userModel.getData().getPhone());
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(model);

        country = Paper.book().read("country", "not_selected");
        if (userModel == null) {
            if (country.equals("not_selected")) {
                country = "em";
                binding.setCountry(getString(R.string.uae));
            } else {
                if (country.equals("em")) {
                    binding.setCountry(getString(R.string.uae));

                } else {
                    binding.setCountry(getString(R.string.turkey));

                }
            }
        } else {
            if (country.equals("not_selected")) {
                if (userModel.getData().getPhone_code().equals("+971")) {
                    country = "em";
                    binding.setCountry(getString(R.string.uae));

                } else {
                    country = "eg";
                    binding.setCountry(getString(R.string.turkey));

                }
            }
        }


        CityModel cityModel = new CityModel(0, getString(R.string.ch_city), getString(R.string.ch_city));
        cityModelList.add(cityModel);
        citySpinnerAdapter = new CitySpinnerAdapter(this, cityModelList, lang);
        binding.spinnerCity.setAdapter(citySpinnerAdapter);


        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    model.setCityModel(null);

                } else {
                    model.setCityModel(cityModelList.get(i));
                }
                binding.setModel(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.btnSend.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                Intent intent = getIntent();
                intent.putExtra("data", model);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.llBack.setOnClickListener(view -> finish());
        getCities(country);

    }

    private void getCities(String country_id) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getCities(lang, country_id)
                .enqueue(new Callback<CityDataModel>() {
                    @Override
                    public void onResponse(Call<CityDataModel> call, Response<CityDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getData() != null) {
                                cityModelList.clear();
                                CityModel cityModel = new CityModel(0, getString(R.string.ch_city), getString(R.string.ch_city));
                                cityModelList.add(cityModel);
                                cityModelList.addAll(response.body().getData());
                                runOnUiThread(() -> citySpinnerAdapter.notifyDataSetChanged());
                            }


                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(CheckOutActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(CheckOutActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CityDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(CheckOutActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CheckOutActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
}