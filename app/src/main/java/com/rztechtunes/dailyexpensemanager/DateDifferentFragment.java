package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.helper.DateCalculator;


public class DateDifferentFragment extends Fragment {

    TickerView dayTV,monthTV,yearTV;
    DatePicker startDP,endDP;
    Button calculateBtn;

    public DateDifferentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_different, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayTV = view.findViewById(R.id.dayTV);
        monthTV = view.findViewById(R.id.monthsTV);
        yearTV = view.findViewById(R.id.yearTV);
        startDP = view.findViewById(R.id.startpiker);
        endDP = view.findViewById(R.id.endPicker);
        calculateBtn = view.findViewById(R.id.dfBtn);

        dayTV.setCharacterLists(TickerUtils.provideNumberList());
        monthTV.setCharacterLists(TickerUtils.provideNumberList());
        yearTV.setCharacterLists(TickerUtils.provideNumberList());

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateCalculator calculateAge=DateCalculator.calculateAge(startDP.getDayOfMonth(),startDP.getMonth()+1,startDP.getYear(),endDP.getDayOfMonth(),endDP.getMonth()+1,endDP.getYear());

                yearTV.setText(""+calculateAge.getYear());
                monthTV.setText(""+calculateAge.getMonth());
                dayTV.setText(""+calculateAge.getDay());

            }
        });
    }
}