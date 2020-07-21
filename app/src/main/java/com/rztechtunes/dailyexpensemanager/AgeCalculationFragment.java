package com.rztechtunes.dailyexpensemanager;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.helper.DateCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.airbnb.lottie.L.TAG;

public class AgeCalculationFragment extends Fragment {
    private InterstitialAd mInterstitialAd;


    TickerView monthTV, dayTV, b_monthTV, b_dayTV;
    TickerView yearTV;
    DatePicker dobDP, currentDP;
    Button calculateBtn;


    public AgeCalculationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_age_calculation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yearTV = view.findViewById(R.id.yearTV);
        monthTV = view.findViewById(R.id.monthsTV);
        dayTV = view.findViewById(R.id.dayTV);
        b_monthTV = view.findViewById(R.id.b_monthTV);
        b_dayTV = view.findViewById(R.id.b_dayTV);
        dobDP = view.findViewById(R.id.dobpiker);
        currentDP = view.findViewById(R.id.currentPicker);
        calculateBtn = view.findViewById(R.id.ageBtn);

        //For TextViewCountAnimation
        yearTV.setCharacterLists(TickerUtils.provideNumberList());
        monthTV.setCharacterLists(TickerUtils.provideNumberList());
        dayTV.setCharacterLists(TickerUtils.provideNumberList());
        b_dayTV.setCharacterLists(TickerUtils.provideNumberList());
        b_monthTV.setCharacterLists(TickerUtils.provideNumberList());


        //Interstitial Add Run
        MobileAds.initialize(getActivity(), getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitalUnitId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateCalculator calculateAge = DateCalculator.calculateAge(dobDP.getDayOfMonth(), dobDP.getMonth() + 1, dobDP.getYear(), currentDP.getDayOfMonth(), currentDP.getMonth() + 1, currentDP.getYear());

                int b_day = dobDP.getDayOfMonth();
                int b_month = dobDP.getMonth();
                int b_year = dobDP.getYear();

                int current_day = currentDP.getDayOfMonth();
                int current_month = currentDP.getMonth();
                int current_year = currentDP.getYear();

                int birthDayLeft = 0, monthLeft = 0;
                if (b_day < current_day) {
                    b_day = b_day + 30;
                    birthDayLeft = b_day - current_day;

                    if (b_month == current_month) {
                        monthLeft = monthLeft + 11;
                    } else {

                        if (b_month < current_month) {
                            b_month = (b_month - 1) + 12;
                            monthLeft = b_month - current_month;
                        }
                    }

                } else {
                    birthDayLeft = b_day - current_day;

                    if (b_month<current_month)
                    {
                        b_month = (b_month ) + 12;
                        monthLeft = b_month - current_month;

                    }
                    else
                    {
                        monthLeft = b_month - current_month;

                    }

                }

                yearTV.setText("" + calculateAge.getYear());
                monthTV.setText("" + calculateAge.getMonth());
                dayTV.setText("" + calculateAge.getDay());

                b_monthTV.setText("" + monthLeft);
                b_dayTV.setText("" + birthDayLeft);


                //Add Load
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }


            }


        });

    }


}