package com.rztechtunes.dailyexpensemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rztechtunes.dailyexpensemanager.R;
import com.rztechtunes.dailyexpensemanager.pojo.CatagoriesSPPojo;
import com.rztechtunes.dailyexpensemanager.pojo.CounrtySPPojo;

import java.util.ArrayList;
import java.util.List;

public class CatagoriesSpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    List<CatagoriesSPPojo> list;

    public CatagoriesSpinnerAdapter(@NonNull Context context, List<CatagoriesSPPojo> list) {
        super(context, R.layout.catagories_spinner_layout);
        this.list = list;

    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.catagories_spinner_layout, parent,false);

            TextView catagoriesName = convertView.findViewById(R.id.catagoriesName);
            ImageView cataImage = convertView.findViewById(R.id.catagoriesImage);

            cataImage.setImageResource(list.get(position).getCataImage());
            catagoriesName.setText(list.get(position).getCataName());

        return convertView;
    }

}
