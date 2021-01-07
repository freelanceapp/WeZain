package com.wezain.ui.activity_add_address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wezain.R;
import com.wezain.databinding.ActivityAddAddressBinding;
import com.wezain.language.Language;
import com.wezain.models.AddAddressModel;
import com.wezain.models.AddressModel;
import com.wezain.models.SelectedLocation;
import com.wezain.models.UserModel;
import com.wezain.mvp.activity_add_address_mvp.ActivityAddAddressPresenter;
import com.wezain.mvp.activity_add_address_mvp.ActivityAddAddressView;
import com.wezain.preferences.Preferences;

import io.paperdb.Paper;

public class AddAddressActivity extends AppCompatActivity implements ActivityAddAddressView {
    private ActivityAddAddressBinding binding;
    private String lang;
    private SelectedLocation selectedLocation;
    private AddressModel addressModel;
    private AddAddressModel addAddressModel;
    private Preferences preferences;
    private UserModel userModel;
    private ActivityAddAddressPresenter presenter;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        selectedLocation = (SelectedLocation) intent.getSerializableExtra("location");
        addressModel = (AddressModel) intent.getSerializableExtra("data");
    }


    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        presenter = new ActivityAddAddressPresenter(this,this);
        addAddressModel = new AddAddressModel();
        addAddressModel.setPhone(userModel.getData().getPhone());
        addAddressModel.setUser_id(String.valueOf(userModel.getData().getId()));
        if (addressModel!=null){
            addAddressModel.setAddress_id(String.valueOf(addressModel.getId()));
            addAddressModel.setAddress(addressModel.getAddress().split("-")[0]);
            if (addressModel.getAddress().split("-").length==2){
                addAddressModel.setAdditionalNote(addressModel.getAddress().split("-")[1]);
            }
            addAddressModel.setLat(Double.parseDouble(addressModel.getGoogle_lat()));
            addAddressModel.setLng(Double.parseDouble(addressModel.getGoogle_long()));
            binding.btnAdd.setText(R.string.update_address);

            if (addressModel.getType().equals("home")){
                binding.flHome.setBackgroundResource(R.drawable.small_stroke_primary);
                binding.flwork.setBackgroundResource(0);
                addAddressModel.setType("home");
            }else {
                binding.flwork.setBackgroundResource(R.drawable.small_stroke_primary);
                binding.flHome.setBackgroundResource(0);
                addAddressModel.setType("work");
            }

        }else {
            addAddressModel.setLat(selectedLocation.getLat());
            addAddressModel.setLng(selectedLocation.getLng());
            addAddressModel.setAddress(selectedLocation.getAddress());
            binding.btnAdd.setText(R.string.add_address);
        }

        binding.setModel(addAddressModel);

        binding.flHome.setOnClickListener(view -> {
            binding.flHome.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flwork.setBackgroundResource(0);
            addAddressModel.setType("home");
        });

        binding.flwork.setOnClickListener(view -> {
            binding.flwork.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flHome.setBackgroundResource(0);
            addAddressModel.setType("work");
        });

        binding.btnAdd.setOnClickListener(view -> {

            if (addressModel==null){
                addAddress();
            }else {
                updateAddress();
            }
        });

        binding.llBack.setOnClickListener(view -> finish());
    }



    private void addAddress() {
        if (addAddressModel.isDataValid(this)){
            presenter.add_address(addAddressModel);

        }
    }

    private void updateAddress() {
        if (addAddressModel.isDataValid(this)){
            presenter.update_address(addAddressModel);

        }

    }


    @Override
    public void onAddedSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onUpdateSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}