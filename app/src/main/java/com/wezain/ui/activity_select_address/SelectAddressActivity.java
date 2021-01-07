package com.wezain.ui.activity_select_address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wezain.R;
import com.wezain.adapters.AddressAdapter;
import com.wezain.databinding.ActivitySelectAddressBinding;
import com.wezain.language.Language;
import com.wezain.models.AddressModel;
import com.wezain.mvp.activity_my_address_mvp.ActivityAddressPresenter;
import com.wezain.mvp.activity_my_address_mvp.ActivityMyAddressView;
import com.wezain.ui.activity_map.MapActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class SelectAddressActivity extends AppCompatActivity implements ActivityMyAddressView {
    private ActivitySelectAddressBinding binding;
    private String lang;
    private ActivityAddressPresenter presenter;
    private AddressAdapter adapter;
    private List<AddressModel> addressModelList;
    private int selectedPos=-1;
    private boolean isFromCart = false;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("cart")){
            isFromCart = true;
        }
    }


    private void initView() {
        addressModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        presenter = new ActivityAddressPresenter(this,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter  = new AddressAdapter(addressModelList,this);
        binding.recView.setAdapter(adapter);

        presenter.getMyAddress();

        binding.llBack.setOnClickListener(view -> {
            finish();
        });
        binding.btnSelect.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivityForResult(intent,100);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            presenter.getMyAddress();
        }
    }

    @Override
    public void onSuccess(List<AddressModel> data) {
        if (data.size()>0){
            addressModelList.addAll(data);
            adapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressShow() {
        addressModelList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);

    }

    @Override
    public void onRemovedSuccess() {
        if (addressModelList.size()>0&&selectedPos!=-1){
            addressModelList.remove(selectedPos);
            adapter.notifyItemRemoved(selectedPos);

            if (addressModelList.size()>0){
                binding.tvNoData.setVisibility(View.GONE);
            }else {
                binding.tvNoData.setVisibility(View.VISIBLE);

            }
        }
    }

    public void updateAddress(AddressModel model, int adapterPosition) {
        Intent intent = new Intent(this,MapActivity.class);
        intent.putExtra("data",model);
        startActivityForResult(intent,100);
    }

    public void deleteAddress(AddressModel model, int adapterPosition) {
        this.selectedPos = adapterPosition;
        presenter.remove_address(model);

    }

    public void setItemSelect(AddressModel model) {
        if (isFromCart){
            Intent intent = getIntent();
            intent.putExtra("data",model);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}