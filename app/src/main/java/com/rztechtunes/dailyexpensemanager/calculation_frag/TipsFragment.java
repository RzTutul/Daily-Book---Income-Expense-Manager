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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.textfield.TextInputEditText;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.R;

import java.text.DecimalFormat;


public class TipsFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    TickerView amountTV,PerPersonTV;
    TextInputEditText amountET, pecentageET,peopleET;
    Button calculateBtn;

    public TipsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amountTV = view.findViewById(R.id.amountTV);
        amountET = view.findViewById(R.id.amountET);
        pecentageET = view.findViewById(R.id.percentageET);
        calculateBtn = view.findViewById(R.id.calculateBtn);
        PerPersonTV = view.findViewById(R.id.perPersonTV);
        peopleET = view.findViewById(R.id.personET);

        amountTV.setCharacterLists(TickerUtils.provideNumberList());
        PerPersonTV.setCharacterLists(TickerUtils.provideNumberList());


        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitalUnitId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountET.getText().toString();
                String percentage = pecentageET.getText().toString();
                String people = peopleET.getText().toString();
                if (amount.equals("")) {
                    amountET.setError("Input Amount.");
                } else if (percentage.equals("")) {
                    pecentageET.setError("Input percentage.");
                }  else if (people.equals("")) {
                    peopleET.setError("Total Person.");
                }
                else {

                    amountTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                    PerPersonTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }



                    double finalAmount = ((Double.valueOf(amount) / 100) * (Double.valueOf(percentage)/100)) * 100;

                    PerPersonTV.setText(decimalFormat.format(finalAmount));
                    double totalAmount = finalAmount*Double.valueOf(people);
                    amountTV.setText(decimalFormat.format(totalAmount));
                }
            }
        });
    }
}