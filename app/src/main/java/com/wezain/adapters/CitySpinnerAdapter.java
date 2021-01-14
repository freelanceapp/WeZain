package com.wezain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.wezain.R;
import com.wezain.databinding.SpinnerRowBinding;
import com.wezain.models.CityModel;
import com.wezain.models.CountryCodeModel;

import java.util.List;

public class CitySpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<CityModel> list;
    private LayoutInflater inflater;
    private String lang;
    public CitySpinnerAdapter(Context context, List<CityModel> list,String lang) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row,null,false);
        binding.setTitle(list.get(i).getCity_name_en());
        return binding.getRoot();
    }
}
