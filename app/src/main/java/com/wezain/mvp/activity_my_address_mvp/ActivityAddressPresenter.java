package com.wezain.mvp.activity_my_address_mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wezain.R;
import com.wezain.models.AddressDataModel;
import com.wezain.models.AddressModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.share.Common;
import com.wezain.tags.Tags;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityAddressPresenter {
    private Context context;
    private ActivityMyAddressView view;
    private Preferences preference;
    private UserModel userModel;

    public ActivityAddressPresenter(Context context, ActivityMyAddressView view) {
        this.context = context;
        this.view = view;
        preference = Preferences.getInstance();
        userModel = preference.getUserData(context);

    }


    public void getMyAddress()
    {
        if (userModel == null) {
            return;
        }
        String user_id  = String.valueOf(userModel.getData().getId());

        view.onProgressShow();
        Api.getService(Tags.base_url)
                .getMyAddress(userModel.getData().getToken(),user_id)
                .enqueue(new Callback<AddressDataModel>() {
                    @Override
                    public void onResponse(Call<AddressDataModel> call, Response<AddressDataModel> response) {
                        view.onProgressHide();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200 && response.body().getData() != null) {
                                view.onSuccess(response.body().getData());

                            }


                        } else {
                            view.onProgressHide();
                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                view.onFailed("Server Error");

                            } else {
                                view.onFailed(context.getString(R.string.failed));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddressDataModel> call, Throwable t) {
                        try {
                            view.onProgressHide();


                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onFailed(context.getString(R.string.something));
                                } else {
                                    view.onFailed(context.getString(R.string.failed));

                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void remove_address(AddressModel addressModel)
    {

        if (userModel==null){
            return;
        }
        ProgressDialog dialog = Common.createProgressDialog(context,context.getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String user_id = String.valueOf(userModel.getData().getId());
        Api.getService(Tags.base_url)
                .deleteAddress(userModel.getData().getToken(),String.valueOf(addressModel.getId()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            if (response.body() != null) {
                                view.onRemovedSuccess();
                            }


                        } else {

                            dialog.dismiss();
                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                view.onFailed(context.getString(R.string.failed));

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
                                    view.onFailed(context.getString(R.string.something));
                                } else {
                                    view.onFailed(context.getString(R.string.failed));

                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
}
