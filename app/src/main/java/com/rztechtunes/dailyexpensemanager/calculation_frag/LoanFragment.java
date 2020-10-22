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
import com.rztechtunes.dailyexpensemanager.R;

import java.text.DecimalFormat;

public class LoanFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    TickerView monthlyTV, totalPayTV, interestTV;
    TextInputEditText loanET, rateET, periodET;
    Button calculateBtn;
    Spinner periodSP;
    String select_period;

    String period[] = {"Monthly", "Yearly"};

    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public LoanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        monthlyTV = view.findViewById(R.id.monthlyTV);
        totalPayTV = view.findViewById(R.id.totalPayTV);
        interestTV = view.findViewById(R.id.interestTV);
        loanET = view.findViewById(R.id.loanET);
        rateET = view.findViewById(R.id.rateET);
        periodET = view.findViewById(R.id.periodET);
        calculateBtn = view.findViewById(R.id.loanbtn);
        periodSP = view.findViewById(R.id.monthYearSp);

        monthlyTV.setCharacterLists(TickerUtils.provideNumberList());
        totalPayTV.setCharacterLists(TickerUtils.provideNumberList());
        interestTV.setCharacterLists(TickerUtils.provideNumberList());



        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.loan_interstitial));
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
                String loan = loanET.getText().toString();
                String rate = rateET.getText().toString();
                String period = periodET.getText().toString();

                if (loan.equals("")) {
                    loanET.setError("Enter Loan");
                } else if (rate.equals("")) {
                    rateET.setError("Enter rate");
                } else if (period.equals("")) {
                    periodET.setError("Enter period");
                } else {

                    double loanValue = 0.0;
                    double rateValue = 0.0;
                    double periodValue = 0.0;
                    double percentage = 0.0;
                    loanValue = Double.parseDouble(loan);
                    rateValue = Double.parseDouble(rate);
                    periodValue = Double.parseDouble(period);
                    percentage = ((loanValue / 100) * (rateValue / 100)) * 100;
                    if (select_period.equals("Monthly")) {

                        double monthlyPay = (loanValue + percentage) / periodValue;
                        double totalPay = loanValue + (percentage * periodValue);
                        double totalInterest = (percentage * periodValue);

                        monthlyTV.setText(String.valueOf(decimalFormat.format(monthlyPay)));
                        totalPayTV.setText(String.valueOf(decimalFormat.format(totalPay)));
                        interestTV.setText(String.valueOf(decimalFormat.format(totalInterest)));

                    } else {
                        double monthlyPay = (loanValue + percentage) / 12;
                        double totalPay = monthlyPay * (12 * periodValue);
                        double totalInterest = percentage * (12 * periodValue);

                        monthlyTV.setText(decimalFormat.format(monthlyPay));
                        totalPayTV.setText(decimalFormat.format(totalPay));
                        interestTV.setText(decimalFormat.format(totalInterest));

                    }

                    monthlyTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    totalPayTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
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