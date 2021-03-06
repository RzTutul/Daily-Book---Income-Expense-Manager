package com.rztechtunes.dailyexpensemanager.calculation_frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.R;

public class SmokingCostFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    TickerView weekCostTV, monthCostTV, yearCostTV;
    EditText perDayET, perCostET;
    Button calculateBtn;

    public SmokingCostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_smoking_cost, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        monthCostTV = view.findViewById(R.id.perMonthTV);
        yearCostTV = view.findViewById(R.id.perYearCost);
        weekCostTV = view.findViewById(R.id.perWeekTV);
        perDayET = view.findViewById(R.id.smokedPerET);
        perCostET = view.findViewById(R.id.costET);
        calculateBtn = view.findViewById(R.id.smokedBtn);

        monthCostTV.setCharacterLists(TickerUtils.provideNumberList());
        yearCostTV.setCharacterLists(TickerUtils.provideNumberList());
        weekCostTV.setCharacterLists(TickerUtils.provideNumberList());

        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.smoking_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String perDay = perDayET.getText().toString();
                String perCost = perCostET.getText().toString();

                if (perDay.equals("")) {
                    perDayET.setError("input cigarettes smoked per day");

                } else if (perCost.equals("")) {
                    perCostET.setError("Cost Per Cigarettes");
                } else {
                    double perMonthCost = (Double.parseDouble(perDay) * Double.parseDouble(perCost)) * 30;
                    double perYearCost = (Double.parseDouble(perDay) * Double.parseDouble(perCost)) * 365;
                    double perWeekCost = (Double.parseDouble(perDay) * Double.parseDouble(perCost)) * 7;

                    monthCostTV.setText(String.valueOf(perMonthCost));
                    yearCostTV.setText(String.valueOf(perYearCost));
                    weekCostTV.setText(String.valueOf(perWeekCost));



                    monthCostTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    yearCostTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    weekCostTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));

                    //Add Load
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }

                }
            }
        });
    }
}