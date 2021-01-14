package com.wezain.mvp.activity_cart_mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wezain.R;
import com.wezain.models.CartDataModel;
import com.wezain.models.SendOrderModel;
import com.wezain.models.SingleOrderModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.share.Common;
import com.wezain.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityCartPresenter {
    private UserModel userModel;
    private Preferences preferences;
    private CartActivityView view;
    private Context context;
    private CartDataModel cartDataModel;

    public ActivityCartPresenter(CartActivityView view, Context context) {
        this.view = view;
        this.context = context;
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(context);
        cartDataModel = preferences.getCartData(context);
        if (cartDataModel==null){
            cartDataModel = new CartDataModel();
            cartDataModel.setProducts(new ArrayList<>());
        }


    }


    public void update_cart(CartDataModel.CartModel cartModel, int amount) {
        int pos = getCartItemPos(cartModel);
        if (pos != -1) {
            cartModel.setAmount(amount);
            List<CartDataModel.CartModel> cartModelList = cartDataModel.getProducts();
            cartModelList.set(pos, cartModel);
            cartDataModel.setProducts(cartModelList);
            calculateTotalCost();
        }

    }

    public void getCartData() {
        view.onDataSuccess(cartDataModel);
        calculateTotalCost();
    }

    public void removeCartItem(CartDataModel.CartModel cartModel) {
        int pos = getCartItemPos(cartModel);
        if (pos != -1) {
            List<CartDataModel.CartModel> cartModelList = cartDataModel.getProducts();
            cartModelList.remove(pos);
            cartDataModel.setProducts(cartModelList);
            preferences.createUpdateCartData(context, cartDataModel);
            calculateTotalCost();
            view.onCartItemRemoved(pos);

        }
    }

    private int getCartItemPos(CartDataModel.CartModel cartModel) {

        int pos = -1;
        for (int index = 0; index < cartDataModel.getProducts().size(); index++) {
            CartDataModel.CartModel model = cartDataModel.getProducts().get(index);


            if (model.getPrice_id().equals(cartModel.getPrice_id())) {
                pos = index;
                return pos;
            }
        }

        return pos;
    }

    private void calculateTotalCost() {
        double total = 0.0;
        for (CartDataModel.CartModel cartModel : cartDataModel.getProducts()) {
            total += cartModel.getPrice() * cartModel.getAmount();
        }


        cartDataModel.setTotal_cost(total);
        preferences.createUpdateCartData(context, cartDataModel);
        view.onCostUpdate(total);

    }


    public void backPress() {

        view.onFinished();


    }

    public void checkOut() {

        if (userModel == null) {
            Common.CreateDialogAlert(context, context.getString(R.string.pls_signin_signup));
        } else {
            view.onCheckOut();
        }


    }




    public void sendOrder(SendOrderModel sendOrderModel,String lang) {
        if (userModel == null && cartDataModel == null) {
            return;
        }

        cartDataModel.setUser_id(String.valueOf(userModel.getData().getId()));
        cartDataModel.setFirst_name(sendOrderModel.getFirst_name());
        cartDataModel.setLast_name(sendOrderModel.getLast_name());
        cartDataModel.setPhone(sendOrderModel.getPhone_code()+""+sendOrderModel.getPhone());
        cartDataModel.setCountry_id(Integer.parseInt(sendOrderModel.getCountryModel().getId()));
        cartDataModel.setCity(sendOrderModel.getCityModel().getCity_name_en());
        cartDataModel.setState(sendOrderModel.getState());
        cartDataModel.setAddress(sendOrderModel.getAddress());
        cartDataModel.setPayment_type("cash");
        cartDataModel.setBill_currency(sendOrderModel.getCountryModel().getCountry());
        cartDataModel.setLang(lang);
        ProgressDialog dialog = Common.createProgressDialog(context, context.getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .sendOrder(userModel.getData().getToken(), cartDataModel)
                .enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getData() != null) {
                                    view.onOrderSendSuccessfully(response.body());
                                    preferences.clearCart(context);
                                    cartDataModel = null;

                                } else {

                                    view.onFailed(context.getString(R.string.failed));

                                }

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
                    public void onFailure(Call<SingleOrderModel> call, Throwable t) {
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
