package com.wezain.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;


import com.squareup.picasso.Picasso;
import com.wezain.R;
import com.wezain.databinding.SliderRowBinding;
import com.wezain.models.SliderModel;
import com.wezain.tags.Tags;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<SliderModel> list ;
    private Context context;
    private LayoutInflater inflater;

    public SliderAdapter(List<SliderModel> list, Context context) {
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
        SliderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.slider_row,container,false);
        Picasso.get().load(Uri.parse(Tags.IMAGE_URL+list.get(position).getMain_image())).into(binding.image);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
