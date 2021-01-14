package com.wezain.mvp.activity_product_details_mvp;


import com.wezain.models.CartDataModel;
import com.wezain.models.ProductModel;

import java.util.List;

public interface ActivityProductDetailsView {

    void onCartUpdated(double totalCost, int itemCount, List<CartDataModel.CartModel> cartModelList);
    void onCartCountUpdated(int count);
    void onAmountSelectedFromCart(int amount);
}
