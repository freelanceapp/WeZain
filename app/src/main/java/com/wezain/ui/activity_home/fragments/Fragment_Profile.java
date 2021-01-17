package com.wezain.ui.activity_home.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wezain.R;
import com.wezain.databinding.DialogCartBinding;
import com.wezain.databinding.DialogCountries2Binding;
import com.wezain.databinding.FragmentProfileBinding;
import com.wezain.interfaces.Listeners;
import com.wezain.models.CartDataModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.share.Common;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_contact_us.ContactUsActivity;
import com.wezain.ui.activity_favorite.FavoriteActivity;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_language.LanguageActivity;
import com.wezain.ui.activity_sign_up.SignUpActivity;
import com.wezain.ui.activity_web_view.WebViewActivity;

import io.paperdb.Paper;

public class Fragment_Profile extends Fragment implements Listeners.ProfileActions{
    private FragmentProfileBinding binding;
    private String lang;
    private String country;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;


    public static Fragment_Profile newInstance(){
        return new Fragment_Profile();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView()
    {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);

        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        country = Paper.book().read("country", "not_selected");

        if (userModel == null) {
            if (country.equals("not_selected")) {
                country = "em";
                binding.imageCountry.setImageResource(R.drawable.flag_ae);
            }else {
                if (country.equals("em")){
                    binding.imageCountry.setImageResource(R.drawable.flag_ae);

                }else {
                    binding.imageCountry.setImageResource(R.drawable.flag_tr);

                }
            }
        } else {
            if (country.equals("not_selected")) {
                if (userModel.getData().getPhone_code().equals("+971")) {
                    country = "em";
                    binding.imageCountry.setImageResource(R.drawable.flag_ae);

                } else {
                    country = "eg";
                    binding.imageCountry.setImageResource(R.drawable.flag_tr);

                }
            }
        }


        binding.setLang(lang);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        if (userModel!=null){
            binding.setModel(userModel);
        }

        binding.setActions(this);
    }

    @Override
    public void onFavorite() {
        if (userModel!=null){

            Intent intent = new Intent(activity, FavoriteActivity.class);
            startActivityForResult(intent,300);
        }else {
            Toast.makeText(activity, getString(R.string.pls_signin_signup), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onChangeLanguage() {
        Intent intent = new Intent(activity, LanguageActivity.class);
        startActivityForResult(intent,100);
    }



    @Override
    public void onContactUs() {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCountry() {
        createCountryDialogAlert();
    }


    @Override
    public void onLogout() {
        activity.logout();
    }

    @Override
    public void onHelp() {
        Intent intent = new Intent(activity, WebViewActivity.class);
        String url = Tags.base_url+lang+"/aboutUs";
        intent.putExtra("url",url);
        startActivity(intent);
    }

    @Override
    public void onUpdateProfile() {
        Intent intent = new Intent(activity, SignUpActivity.class);
        startActivityForResult(intent,200);
    }


    private void createCountryDialogAlert()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogCountries2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_countries2, null, false);

        if (country.equals("em")){
            binding.cardEm.setBackgroundResource(R.drawable.small_stroke_gray2);
            binding.cardTr.setBackgroundResource(0);
        }else {
            binding.cardEm.setBackgroundResource(0);
            binding.cardTr.setBackgroundResource(R.drawable.small_stroke_gray2);;
        }

        binding.cardEm.setOnClickListener(view -> {

            if(!country.equals("em")){
                Paper.init(activity);
                Paper.book().write("country","em");
                country = "em";
                Fragment_Profile.this.binding.imageCountry.setImageResource(R.drawable.flag_ae);

                Preferences preferences = Preferences.getInstance();
                CartDataModel cartDataModel = preferences.getCartData(activity);

                if (cartDataModel==null){
                   activity.refreshFragmentHome();
                }else {
                    if (cartDataModel.getProducts()!=null&&cartDataModel.getProducts().size()>0){
                        createDialogAlert();

                    }else {
                        activity.refreshFragmentHome();

                    }
                }



            }



            dialog.dismiss();


        });


        binding.cardTr.setOnClickListener(view -> {
            if(!country.equals("eg")){
                Paper.init(activity);
                Paper.book().write("country","eg");
                country = "eg";
                Fragment_Profile.this.binding.imageCountry.setImageResource(R.drawable.flag_tr);

                Preferences preferences = Preferences.getInstance();
                CartDataModel cartDataModel = preferences.getCartData(activity);

                if (cartDataModel==null){
                    activity.refreshFragmentHome();
                }else {
                    if (cartDataModel.getProducts()!=null&&cartDataModel.getProducts().size()>0){
                        createDialogAlert();

                    }else {
                        activity.refreshFragmentHome();

                    }
                }
            }


            dialog.dismiss();
        });




        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void createDialogAlert()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogCartBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_cart, null, false);

        binding.tvMsg.setText(R.string.will_delete_cart);
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()
        );

        binding.btnDelete.setOnClickListener(v -> {
                    Preferences preferences = Preferences.getInstance();
                    preferences.clearCart(activity);
                    activity.refreshFragmentHome();
                    dialog.dismiss();

                }
        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK&&data!=null){
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);
        }else if (requestCode==200&&resultCode== Activity.RESULT_OK){
            userModel = preferences.getUserData(activity);
            binding.setModel(userModel);
        }else if (requestCode==300&&resultCode== Activity.RESULT_OK){
            activity.refreshFragmentHome();
        }

    }

}
