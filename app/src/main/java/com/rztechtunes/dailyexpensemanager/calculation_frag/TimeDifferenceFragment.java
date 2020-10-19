package com.rztechtunes.dailyexpensemanager.calculation_frag;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.airbnb.lottie.L.TAG;


public class TimeDifferenceFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    TickerView minutesTV, hoursTV;
    TimePicker startTP, endTP;
    Button calculateBtn;

    int startHour;
    int startMinute;
    int endHour;
    int endMinute;
    String startTime;
    String endTime;


    public TimeDifferenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_difference, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        minutesTV = view.findViewById(R.id.minutesTV);
        hoursTV = view.findViewById(R.id.hoursTV);
        startTP = view.findViewById(R.id.startpiker);
        endTP = view.findViewById(R.id.endPicker);
        calculateBtn = view.findViewById(R.id.timeBtn);

        //For TextViewAnimation
        minutesTV.setCharacterLists(TickerUtils.provideNumberList());
        hoursTV.setCharacterLists(TickerUtils.provideNumberList());


        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitalUnitId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    startHour = startTP.getHour();
                    startMinute = startTP.getMinute();

                    endHour = endTP.getHour();
                    endMinute = endTP.getMinute();


                    startTime = startHour+":"+startMinute;
                    endTime = endHour+":"+endMinute;


                } else {
                    startHour = startTP.getCurrentHour();
                    startMinute = startTP.getCurrentMinute();

                    endHour = endTP.getCurrentHour();
                    endMinute = endTP.getCurrentMinute();
                    startTime = startHour+":"+startMinute;
                    endTime = endHour+":"+endMinute;

                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                try {
                    Date startDate = simpleDateFormat.parse(startTime);
                    Date endDate = simpleDateFormat.parse(endTime);

                    long difference = endDate.getTime() - startDate.getTime();
                    if(difference<0)
                    {
                        Date dateMax = simpleDateFormat.parse("24:00");
                        Date dateMin = simpleDateFormat.parse("00:00");
                        difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                    }
                    int days = (int) (difference / (1000*60*60*24));
                    int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                    Log.i("log_tag","Hours: "+hours+", Mins: "+min);
                    hoursTV.setText(String.valueOf(hours));
                    minutesTV.setText(String.valueOf(min));

                } catch (ParseException e) {
                    e.printStackTrace();
                }



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