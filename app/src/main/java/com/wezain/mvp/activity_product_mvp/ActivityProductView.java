package com.wezain.mvp.activity_product_mvp;

import com.wezain.models.ProductModel;

import java.util.List;

public interface ActivityProductView {
    void onSuccess(List<ProductModel> data);
    void onFailed(String msg);
    void onProgressShow();
    void onProgressHide();

}
