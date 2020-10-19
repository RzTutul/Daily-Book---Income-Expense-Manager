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
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.R;

import java.text.DecimalFormat;


public class WagesCalculationFragment extends Fragment {
    private InterstitialAd mInterstitialAd;
    TickerView yearTV, monthTV, weekTV, dailyTV, hourlyTV;
    EditText salaryET, weekET, hourET;
    Spinner salaryofferSp;
    Button calculateBtn;
    String select_salaryOffer;

    String salaryOffer[] = {"Monthly", "Hourly", "Daily"};
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public WagesCalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wages_calculation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yearTV = view.findViewById(R.id.w_yearTV);
        monthTV = view.findViewById(R.id.w_monthTV);
        weekTV = view.findViewById(R.id.w_weekTV);
        dailyTV = view.findViewById(R.id.w_dailyTV);
        hourlyTV = view.findViewById(R.id.w_hourlyTV);
        salaryET = view.findViewById(R.id.salaryET);
        weekET = view.findViewById(R.id.dayOfWeekET);
        hourET = view.findViewById(R.id.hourET);
        calculateBtn = view.findViewById(R.id.wagesBtn);
        salaryofferSp = view.findViewById(R.id.salaryofferSp);

        yearTV.setCharacterLists(TickerUtils.provideNumberList());
        monthTV.setCharacterLists(TickerUtils.provideNumberList());
        weekTV.setCharacterLists(TickerUtils.provideNumberList());
        dailyTV.setCharacterLists(TickerUtils.provideNumberList());
        hourlyTV.setCharacterLists(TickerUtils.provideNumberList());


        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitalUnitId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, salaryOffer);

        salaryofferSp.setAdapter(adapter);

        salaryofferSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_salaryOffer = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String salary = salaryET.getText().toString();
                String dayofWeek = weekET.getText().toString();
                String hour = hourET.getText().toString();

                if (salary.equals("")) {
                    salaryET.setError("Enter your salary!");
                } else if (dayofWeek.equals("")) {
                    weekET.setError("Days of Week !");
                } else if (hour.equals("")) {
                    hourET.setError("Total hour PerDay!");
                } else {

                    double salaryValue = Double.parseDouble(salary);
                    double weekValue = Double.parseDouble(dayofWeek);
                    double hourValue = Double.parseDouble(hour);
                    double perday = 0.0;
                    double weekly = 0.0;
                    double hourly = 0.0;
                    double yearly = 0.0;
                    double monthly = 0.0;

                    if (select_salaryOffer.equals("Monthly")) {
                        monthTV.setText(String.valueOf(salaryValue));
                        yearTV.setText("" + (salaryValue * 12.0));
                        perday = salaryValue / (weekValue * 4);
                        weekly = (perday * weekValue);
                        hourly = (perday / hourValue);
                        dailyTV.setText(decimalFormat.format(perday));
                        weekTV.setText(decimalFormat.format(weekly));
                        hourlyTV.setText(decimalFormat.format(hourly));
                    } else if (select_salaryOffer.equals("Daily")) {
                        dailyTV.setText(String.valueOf(salaryValue));
                        monthly = (salaryValue * ((weekValue * 4) + 2));
                        yearly = salaryValue * (((weekValue * 4) + 2) * 12);
                        weekly = salaryValue * weekValue;
                        hourly = (salaryValue / hourValue);
                        yearTV.setText(decimalFormat.format(yearly));
                        weekTV.setText(decimalFormat.format(weekly));
                        hourlyTV.setText(decimalFormat.format(hourly));
                        monthTV.setText(decimalFormat.format(monthly));
                    } else if (select_salaryOffer.equals("Hourly")) {
                        hourlyTV.setText(String.valueOf(salaryValue));
                        perday = hourValue * salaryValue;
                        weekly = salaryValue * (weekValue * hourValue);
                        monthly = (salaryValue * (((weekValue * 4) + 2) * hourValue));
                        yearly = salaryValue * ((((weekValue * 4) + 2) * hourValue) * 12);

                        dailyTV.setText(decimalFormat.format(perday));
                        yearTV.setText(decimalFormat.format(yearly));
                        weekTV.setText(decimalFormat.format(weekly));
                        monthTV.setText(decimalFormat.format(monthly));
                    }

                    dailyTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    hourlyTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    yearTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    weekTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));
                    monthTV.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark));


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