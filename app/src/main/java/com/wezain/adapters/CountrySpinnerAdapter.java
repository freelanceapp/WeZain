package com.wezain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;

import com.wezain.R;
import com.wezain.databinding.SpinnerRowBinding;
import com.wezain.models.CountryCodeModel;

import java.util.List;

public class CountrySpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<CountryCodeModel> list;
    private LayoutInflater inflater;

    public CountrySpinnerAdapter(Context context, List<CountryCodeModel> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        binding.setTitle(list.get(i).getName());
        return binding.getRoot();
    }
}
