package com.wezain.mvp.activity_product_details_mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wezain.models.CartDataModel;
import com.wezain.models.ProductModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityProductDetailsPresenter {
    private Context context;
    private ActivityProductDetailsView view;
    private Preferences preference;
    private UserModel userModel;
    private List<CartDataModel.CartModel> cartModelList;
    private CartDataModel cartDataModel;

    public ActivityProductDetailsPresenter(Context context, ActivityProductDetailsView view) {
        this.context = context;
        this.view = view;
        preference = Preferences.getInstance();
        userModel = preference.getUserData(context);
        cartDataModel = preference.getCartData(context);
        if (cartDataModel==null){

            cartModelList = new ArrayList<>();
            cartDataModel = new CartDataModel();
            cartDataModel.setProducts(cartModelList);
        }else {
            if (cartDataModel.getProducts()==null){
                cartModelList = new ArrayList<>();

            }else {
                cartModelList = cartDataModel.getProducts();

            }

        }

    }

    public void add_to_cart(ProductModel.Product_Prices product_prices, ProductModel productModel, int amount)
    {
        int pos = isProductItemSelected(product_prices);

        if (pos==-1){

            CartDataModel.CartModel cartModel = new CartDataModel.CartModel(String.valueOf(product_prices.getId()),productModel.getMain_image(),productModel.getTitle(),amount,Double.parseDouble(product_prices.getPrice()));
            cartModelList.add(cartModel);
        }else {
            CartDataModel.CartModel cartModel = cartModelList.get(pos);
            cartModel.setAmount(amount);
            cartModelList.set(pos,cartModel);
        }
        if (cartDataModel==null){
            cartDataModel = new CartDataModel();
        }

        cartDataModel.setProducts(cartModelList);

        calculateTotalCost();
    }

    private void calculateTotalCost() {
        double total =0.0;
        for (CartDataModel.CartModel cartModel:cartModelList){
            total += cartModel.getAmount()*cartModel.getPrice();
        }
        cartDataModel.setTotal_cost(total);
        preference.createUpdateCartData(context,cartDataModel);
        view.onCartUpdated(total,cartModelList.size(),cartModelList);
    }


    public int isProductItemSelected(ProductModel.Product_Prices product_prices){

        int pos = -1;

        cartDataModel = preference.getCartData(context);
        if (cartDataModel!=null&&cartDataModel.getProducts()!=null)
        {
            cartModelList = cartDataModel.getProducts();
            for (int index =0;index<cartModelList.size();index++){
                CartDataModel.CartModel cartModel = cartModelList.get(index);
                if (String.valueOf(product_prices.getId()).equals(cartModel.getPrice_id())){
                    pos = index;
                    return pos;
                }
            }
        }


        return pos;
    }

    public void getCartCount(){
        cartDataModel = preference.getCartData(context);
        if (cartDataModel!=null&&cartDataModel.getProducts()!=null){
            view.onCartCountUpdated(cartDataModel.getProducts().size());

        }else {
            view.onCartCountUpdated(0);

        }
    }

    public void getItemAmount(ProductModel.Product_Prices product_prices){
        int pos = isProductItemSelected(product_prices);
        if (pos==-1){
            view.onAmountSelectedFromCart(1);
        }else {
            view.onAmountSelectedFromCart(cartDataModel.getProducts().get(pos).getAmount());
        }

    }


}
