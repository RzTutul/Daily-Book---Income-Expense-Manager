package com.rztechtunes.dailyexpensemanager.calculation_frag;

import android.graphics.Color;
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
import com.google.android.material.textfield.TextInputLayout;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.R;

import java.text.DecimalFormat;

public class PercentageFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    TextInputLayout amountTI;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    TickerView amountTV,discountTV;
    TextView detilsTV;
    TextInputEditText amountET, pecentageET;
    Button calculateBtn;

    public PercentageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_percentage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        amountTV = view.findViewById(R.id.amountTV);
        discountTV = view.findViewById(R.id.discountTV);
        amountET = view.findViewById(R.id.amountET);
        pecentageET = view.findViewById(R.id.percentageET);
        calculateBtn = view.findViewById(R.id.calculateBtn);
        detilsTV = view.findViewById(R.id.detilsTV);
        amountTI = view.findViewById(R.id.amountTI);

        amountTV.setCharacterLists(TickerUtils.provideNumberList());
        discountTV.setCharacterLists(TickerUtils.provideNumberList());


        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.percentageUnitId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountET.getText().toString();
                String percentage = pecentageET.getText().toString();
                if (amount.equals("")) {
                    amountET.setError("Input Amount.");
                }
                else if (percentage.equals("")) {
                    pecentageET.setError("Input percentage.");
                }
                else {

                    double discountAmount = ((Double.valueOf(amount) / 100) * (Double.valueOf(percentage)/100)) * 100;
                    double totalAmount =( Double.parseDouble(amount) - discountAmount);

                    discountTV.setText(decimalFormat.format(discountAmount));
                    amountTV.setText(decimalFormat.format(totalAmount));
                    detilsTV.setText(percentage+" percent of "+amount+" is "+decimalFormat.format(discountAmount));
                    amountTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                    discountTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

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