package com.wezain.ui.activity_search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wezain.R;
import com.wezain.adapters.FavoriteProductAdapter;
import com.wezain.adapters.SearchProductAdapter;
import com.wezain.databinding.ActivityFavoriteBinding;
import com.wezain.databinding.ActivitySearchBinding;
import com.wezain.language.Language;
import com.wezain.models.ProductDataModel;
import com.wezain.models.ProductModel;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    private String lang;
    private List<ProductModel> productModelList;
    private SearchProductAdapter adapter;
    private boolean isDataChanged = false;
    private Preferences preferences;
    private UserModel userModel;
    private String country;
    private Call<List<ProductModel>> call;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
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
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SearchProductAdapter(productModelList, this, country);
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(view -> onBackPressed());
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()) {
                    search(editable.toString());
                } else {
                    if (call != null) {
                        call.cancel();
                    }
                    productModelList.clear();
                    adapter.notifyDataSetChanged();
                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.progBar.setVisibility(View.GONE);
                }
            }
        });
        binding.edtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.edtSearch.getText().toString();
                if (!query.isEmpty()) {
                    search(query);
                }
            }
            return false;
        });

    }

    private void search(String query) {
        productModelList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);

        String user_id = null;
        if (userModel != null) {
            user_id = String.valueOf(userModel.getData().getId());
        }

        if (call != null) {
            call.cancel();
        }
        call = Api.getService(Tags.base_url)
                .search(lang, country, user_id, query);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        SearchActivity.this.onSuccess(response.body());
                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);
                    }


                } else {
                    binding.progBar.setVisibility(View.GONE);

                    switch (response.code()) {
                        case 500:
                            Toast.makeText(SearchActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            break;

                        default:
                            Toast.makeText(SearchActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                try {
                    binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(SearchActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    private void onSuccess(List<ProductModel> data) {
        productModelList.clear();
        if (data.size() > 0) {
            productModelList.addAll(data);
            binding.tvNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        if (isDataChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            isDataChanged = true;
        }
    }

    public void setItemData(ProductModel model) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("data", model);
        startActivityForResult(intent, 100);
    }
}