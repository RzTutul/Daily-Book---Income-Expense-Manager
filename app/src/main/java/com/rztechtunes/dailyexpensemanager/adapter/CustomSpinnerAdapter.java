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
import com.rztechtunes.dailyexpensemanager.pojo.CounrtySPPojo;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    List<CounrtySPPojo> list = new ArrayList<>();

    public CustomSpinnerAdapter(@NonNull Context context, List<CounrtySPPojo> list) {
        super(context, R.layout.county_spinner_layout);
        this.context = context;
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

        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = inflater.inflate(R.layout.county_spinner_layout, parent,false);

            TextView countyName = convertView.findViewById(R.id.countyName);
            TextView DolarName = convertView.findViewById(R.id.dolarName);
            ImageView countyImages = convertView.findViewById(R.id.countyImages);

            countyImages.setImageResource(list.get(position).getCountryImages());
            countyName.setText(list.get(position).getCountyName());
            DolarName.setText(list.get(position).getDolarName());

        }

        return convertView;
    }

}
