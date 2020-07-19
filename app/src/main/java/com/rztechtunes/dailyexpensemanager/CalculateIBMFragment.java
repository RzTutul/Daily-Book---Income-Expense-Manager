package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
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
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;

import static com.airbnb.lottie.L.TAG;


public class CalculateIBMFragment extends Fragment {
    private InterstitialAd mInterstitialAd;

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    NestedScrollView nestedScrollView;
    EditText weightET, feetET, inchET;
    Spinner weightSP;
    SpeedView speedometer;
    Button calculateBtn;

    String Weight[] = {"Kg","Ibs"};
    String selectWeight;
    double bmiResult;


    public CalculateIBMFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculate_i_b_m, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        speedometer = view.findViewById(R.id.speedView);
        weightET = view.findViewById(R.id.weightET);
        feetET = view.findViewById(R.id.feetET);
        inchET = view.findViewById(R.id.inchET);
        calculateBtn = view.findViewById(R.id.bmiCalculateBtn);
        weightSP = view.findViewById(R.id.selectWeightSP);
        nestedScrollView = view.findViewById(R.id.nestedView);



        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.bmiUnitID));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Weight);
        weightSP.setAdapter(adapter);


        weightSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectWeight = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bmiResult =0.0;
                String weight = weightET.getText().toString().trim();
                String feet = feetET.getText().toString().trim();
                String inch = inchET.getText().toString().trim();

                if (weight.equals("")) {
                    weightET.setError("Enter Weight");
                } else if (feet.equals("")) {
                    feetET.setError("Enter Feet");
                } else if (inch.equals("")) {
                    inchET.setError("Enter inch");
                }
                else
                {
                    nestedScrollView.fullScroll(View.FOCUS_DOWN);

                    if (selectWeight.equals("Kg"))
                    {
                        double weightValue  = (Double.parseDouble(weight)*2.20462);
                        double heightValue = ((Double.parseDouble(feet)*12.0)+Double.parseDouble(inch));

                        bmiResult = (weightValue/(heightValue*heightValue))*703;




                    }
                    else
                    {

                        double weightValue  = (Double.parseDouble(weight));
                        double heightValue = ((Double.parseDouble(feet)*12.0)+Double.parseDouble(inch));

                        bmiResult = (weightValue/(heightValue*heightValue))*703;
                    }

                    if (bmiResult>48)
                    {
                        speedometer.setMaxSpeed(240);
                        bmiResult = Double.parseDouble(decimalFormat.format(bmiResult));
                        speedometer.speedTo((float) bmiResult);
                    }
                    else
                    {
                        speedometer.setMaxSpeed(48);
                        bmiResult = Double.parseDouble(decimalFormat.format(bmiResult));
                        speedometer.speedTo((float) bmiResult);
                    }

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