package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;


import com.wezain.R;
import com.wezain.databinding.SliderRow2Binding;
import com.wezain.models.ProductModel;

import java.util.List;

public class ProductSliderAdapter extends PagerAdapter {
    private List<ProductModel.Product_Images> list ;
    private Context context;
    private LayoutInflater inflater;

    public ProductSliderAdapter(List<ProductModel.Product_Images> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SliderRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.slider_row2,container,false);
        binding.setPhoto(list.get(position).getName());
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
