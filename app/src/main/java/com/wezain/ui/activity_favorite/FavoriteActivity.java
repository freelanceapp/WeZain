package com.wezain.ui.activity_favorite;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.wezain.R;
import com.wezain.adapters.FavoriteProductAdapter;
import com.wezain.databinding.ActivityFavoriteBinding;
import com.wezain.language.Language;
import com.wezain.models.CategoryDataModel;
import com.wezain.models.ProductDataModel;
import com.wezain.models.ProductModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.share.Common;
import com.wezain.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoriteBinding binding;
    private String lang;
    private List<ProductModel> productModelList;
    private FavoriteProductAdapter adapter;
    private boolean isDataChanged = false;
    private int selectedPos = -1;
    private Preferences preferences;
    private UserModel userModel;
    private String country;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        country = Paper.book().read("country", "not_selected");

        if (userModel == null) {
            if (country.equals("not_selected")) {
                country = "em";
            }
        } else {
            if (country.equals("not_selected")) {
                if (userModel.getData().getPhone_code().equals("+971")) {
                    country = "em";
                } else {
                    country = "eg";
                }
            }
        }
        productModelList = new ArrayList<>();
        binding.setLang(lang);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new FavoriteProductAdapter(productModelList, this, country);
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(view -> onBackPressed());

        getProducts();

    }

    private void getProducts() {
        Api.getService(Tags.base_url)
                .getFavourite(lang, country, String.valueOf(userModel.getData().getId()))
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                if (response.body().getData().size() > 0) {
                                    FavoriteActivity.this.onSuccess(response.body().getData());

                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(FavoriteActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;

                                default:
                                    Toast.makeText(FavoriteActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(FavoriteActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(FavoriteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void onSuccess(List<ProductModel> data) {

        if (data.size() > 0) {
            productModelList.addAll(data);
            binding.tvNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void onRemoveFavoriteSuccess() {
        isDataChanged = true;
        if (productModelList.size() > 0 && selectedPos != -1) {
            productModelList.remove(selectedPos);
            adapter.notifyItemRemoved(selectedPos);
            if (productModelList.size() > 0) {
                binding.tvNoData.setVisibility(View.GONE);
            } else {
                binding.tvNoData.setVisibility(View.VISIBLE);

            }
            selectedPos = -1;
        }
    }

    @Override
    public void onBackPressed() {
        if (isDataChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }

    public void removeFavorite(ProductModel model, int adapterPosition) {
        selectedPos = adapterPosition;

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String user_id = String.valueOf(userModel.getData().getId());
        Api.getService(Tags.base_url)
                .add_remove_favorite("Bearer " + userModel.getData().getToken(), user_id, String.valueOf(model.getId()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                FavoriteActivity.this.onRemoveFavoriteSuccess();
                            }


                        } else {
                            dialog.dismiss();

                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (response.code() == 500) {
                                Toast.makeText(FavoriteActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FavoriteActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(FavoriteActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FavoriteActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
}