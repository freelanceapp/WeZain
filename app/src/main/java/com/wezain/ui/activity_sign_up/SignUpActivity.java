package com.wezain.ui.activity_sign_up;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.squareup.picasso.Picasso;
import com.wezain.R;
import com.wezain.adapters.CountriesAdapter;
import com.wezain.databinding.ActivitySignUpBinding;
import com.wezain.databinding.DialogCountriesBinding;
import com.wezain.databinding.DialogSelectImageBinding;
import com.wezain.language.Language;
import com.wezain.models.CountryCodeModel;
import com.wezain.models.SignUpModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.share.Common;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_login.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int READ_REQ = 1, CAMERA_REQ = 2;
    private Uri uri = null;
    private SignUpModel model;
    private AlertDialog dialog;
    private Preferences preferences;
    private UserModel userModel;
    private CountriesAdapter adapter;
    private AlertDialog dialog2;
    private List<CountryCodeModel> countryCodeModelList;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();

    }

    private void initView() {
        countryCodeModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        model = new SignUpModel();
        if (userModel!=null){
            model.setPhone_code(userModel.getData().getPhone_code());
            model.setPhone(userModel.getData().getPhone());
            model.setFirst_name(userModel.getData().getFirst_name());
            model.setLast_name(userModel.getData().getLast_name());
            model.setEmail(userModel.getData().getEmail());
            model.setPassword("123456");
            binding.btnSignUp.setText(R.string.update_profile);
        }
        binding.setModel(model);

        CountryCodeModel m1 = new CountryCodeModel("205","+971",getString(R.string.uae),"em");
        CountryCodeModel m2 = new CountryCodeModel("197","+90",getString(R.string.turkey),"eg");
        countryCodeModelList.add(m1);
        countryCodeModelList.add(m2);

        binding.flSelectImage.setOnClickListener(view -> {
            dialog.show();
        });

        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                if (uri == null) {
                    if (userModel==null){
                        signUpWithoutImage();

                    }else {
                        updateProfileWithoutImage();
                    }
                } else {
                    if (userModel==null){
                        signUpWithImage();

                    }else {
                        updateProfileWithImage();
                    }

                }
            }
        });

        binding.tvCode.setOnClickListener(view -> {
           dialog2.show();
        });


        binding.tvLogin.setOnClickListener(view -> finish());

        createImageDialogAlert();
        createCountriesDialog();
    }



    private void createCountriesDialog() {

        dialog2 = new AlertDialog.Builder(this)
                .create();
        adapter = new CountriesAdapter(countryCodeModelList,this);

        DialogCountriesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_countries, null, false);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        dialog2.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setView(binding.getRoot());

    }


    private void signUpWithImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody first_name_part = Common.getRequestBodyText(model.getFirst_name());
        RequestBody last_name_part = Common.getRequestBodyText(model.getLast_name());
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody email_part = Common.getRequestBodyText(model.getEmail());
        RequestBody password_part = Common.getRequestBodyText(model.getPassword());
        MultipartBody.Part image = Common.getMultiPart(this, uri, "logo");


        Api.getService(Tags.base_url)
                .signUpWithImage(first_name_part,last_name_part,phone_code_part,phone_part,email_part,password_part,image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            preferences = Preferences.getInstance();
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            navigateToHomeActivity();
                        } else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if(response.code()==422){
                                Toast.makeText(SignUpActivity.this, R.string.em_ph_exist, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void signUpWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .signUpWithoutImage(model.getFirst_name(), model.getLast_name(), model.getPhone_code(), model.getPhone(), model.getEmail(), model.getPassword())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            preferences = Preferences.getInstance();
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            navigateToHomeActivity();

                        } else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if(response.code()==422){
                                Toast.makeText(SignUpActivity.this, R.string.em_ph_exist, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateProfileWithoutImage() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateProfileWithoutImage(String.valueOf(userModel.getData().getId()),model.getFirst_name(), model.getLast_name(), model.getPhone_code(), model.getPhone(), model.getEmail())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            preferences = Preferences.getInstance();
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            setResult(RESULT_OK);
                            finish();

                        } else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if(response.code()==422){
                                Toast.makeText(SignUpActivity.this, R.string.em_ph_exist, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateProfileWithImage() {

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody id_part = Common.getRequestBodyText(String.valueOf(userModel.getData().getId()));
        RequestBody first_name_part = Common.getRequestBodyText(model.getFirst_name());
        RequestBody last_name_part = Common.getRequestBodyText(model.getLast_name());
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody email_part = Common.getRequestBodyText(model.getEmail());
        MultipartBody.Part image = Common.getMultiPart(this, uri, "logo");


        Api.getService(Tags.base_url)
                .updateProfileWithImage(id_part,first_name_part,last_name_part,phone_code_part,phone_part,email_part,image)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            preferences = Preferences.getInstance();
                            preferences.create_update_userdata(SignUpActivity.this, response.body());
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }else if(response.code()==422){
                                Toast.makeText(SignUpActivity.this, R.string.em_ph_exist, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void createImageDialogAlert() {
        dialog = new AlertDialog.Builder(this)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_select_image, null, false);
        binding.btnCamera.setOnClickListener(view -> {
            dialog.dismiss();
            checkCameraPermission();
        });
        binding.btnGallery.setOnClickListener(view -> {
            dialog.dismiss();
            checkReadPermission();
        });
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
    }

    public void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, READ_REQ);
        } else {
            SelectImage(READ_REQ);
        }
    }

    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage(CAMERA_REQ);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);
        }
    }

    private void SelectImage(int req) {

        Intent intent = new Intent();

        if (req == READ_REQ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, req);

        } else if (req == CAMERA_REQ) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CAMERA_REQ) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                SelectImage(requestCode);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQ && resultCode == Activity.RESULT_OK && data != null) {

            uri = data.getData();
            File file = new File(Common.getImagePath(this, uri));
            Picasso.get().load(file).fit().into(binding.image);
            binding.icon.setVisibility(View.GONE);
            model.setImageUrl(uri.toString());
            binding.setModel(model);

        } else if (requestCode == CAMERA_REQ && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            uri = getUriFromBitmap(bitmap);
            binding.icon.setVisibility(View.GONE);

            if (uri != null) {
                model.setImageUrl(uri.toString());
                binding.setModel(model);
                String path = Common.getImagePath(this, uri);
                if (path != null) {
                    Picasso.get().load(new File(path)).fit().into(binding.image);

                } else {
                    Picasso.get().load(uri).fit().into(binding.image);

                }
            }


        }

    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


    public void setItemData(CountryCodeModel countryCodeModel) {
        binding.tvCode.setText(countryCodeModel.getCode());
        model.setPhone_code(countryCodeModel.getCode());
        binding.setModel(model);
        dialog2.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}