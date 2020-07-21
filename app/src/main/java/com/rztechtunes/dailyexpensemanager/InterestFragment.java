package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.DecimalFormat;

public class InterestFragment extends Fragment {
    private InterstitialAd mInterstitialAd;
    TickerView finalAmountTV, interestTV;
    TextInputEditText initialAmountET, rateET, periodET;
    Button calculateBtn;
    Spinner periodSP;
    String select_period;
    String period[] = {"Monthly", "Yearly"};

    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public InterestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finalAmountTV = view.findViewById(R.id.finalAmountTV);
        interestTV = view.findViewById(R.id.interstTV);
        initialAmountET = view.findViewById(R.id.initialET);
        rateET = view.findViewById(R.id.rateET);
        periodET = view.findViewById(R.id.periodET);
        calculateBtn = view.findViewById(R.id.calculateBtn);
        periodSP = view.findViewById(R.id.monthYearSp);
        calculateBtn = view.findViewById(R.id.interstBtn);

        finalAmountTV.setCharacterLists(TickerUtils.provideNumberList());
        interestTV.setCharacterLists(TickerUtils.provideNumberList());




        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitalUnitId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, period);
        periodSP.setAdapter(adapter);


        periodSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_period = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String initial = initialAmountET.getText().toString();
                String rate = rateET.getText().toString();
                String period = periodET.getText().toString();

                if (initial.equals("")) {
                    initialAmountET.setError("Enter Amount");
                } else if (rate.equals("")) {
                    rateET.setError("Enter rate");
                } else if (period.equals("")) {
                    periodET.setError("Enter period");
                } else {


                    double initalValue = Double.parseDouble(initial);
                    double rateValue = Double.parseDouble(rate);
                    double periodValue = Double.parseDouble(period);
                    double percentage = ((initalValue / 100) * (rateValue / 100)) * 100;
                    if (select_period.equals("Monthly")) {
                        double interst = percentage * periodValue;
                        double finalValue = (initalValue + interst);
                        finalAmountTV.setText(decimalFormat.format(finalValue));
                        interestTV.setText(decimalFormat.format(interst));

                    } else {
                        double interst = percentage * (periodValue * 12);
                        double finalValue = (initalValue + interst);

                        finalAmountTV.setText(decimalFormat.format(finalValue));
                        interestTV.setText(decimalFormat.format(interst));

                    }

                    finalAmountTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    interestTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
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