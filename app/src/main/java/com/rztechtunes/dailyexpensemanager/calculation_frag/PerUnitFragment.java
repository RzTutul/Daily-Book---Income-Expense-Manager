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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PerUnitFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    EditText priceET, qytET;
    TickerView perUnitTV;
    TextView detailsTV;
    Button calculateBtn;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    List<String> detailsList = new ArrayList<>();
    String history = "";

    public PerUnitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_per_unit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        priceET = view.findViewById(R.id.totalPriceET);
        qytET = view.findViewById(R.id.totalQytET);
        perUnitTV = view.findViewById(R.id.perUnitTV);
        calculateBtn = view.findViewById(R.id.perUnitBtn);
        detailsTV = view.findViewById(R.id.detilsTV);
        perUnitTV.setCharacterLists(TickerUtils.provideNumberList());

        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.perUnit_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = priceET.getText().toString();
                String qyt = qytET.getText().toString();
                if (price.equals("")) {
                    priceET.setError("Enter price");
                } else if (qyt.equals("")) {
                    qytET.setError("Enter Qyt");
                } else {

                    double perUnitPrice = Double.parseDouble(price) / Double.parseDouble(qyt);
                    perUnitTV.setText(decimalFormat.format(perUnitPrice));
                    detailsList.add("Price: " + price + " QYT: " + qyt + " PerUnit: " + decimalFormat.format(perUnitPrice) + "\n");

                }

                history = history + detailsList.get(detailsList.size() - 1);

                detailsTV.setText(history);

                perUnitTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));


                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }


            }
        });

    }
}