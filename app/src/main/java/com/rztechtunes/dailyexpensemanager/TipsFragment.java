package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;


public class TipsFragment extends Fragment {

    TextView amountTV,detilsTV,PerPersonTV;
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
        detilsTV = view.findViewById(R.id.detilsTV);
        PerPersonTV = view.findViewById(R.id.perPersonTV);
        peopleET = view.findViewById(R.id.personET);


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountET.getText().toString();
                String percentage = pecentageET.getText().toString();
                String people = peopleET.getText().toString();
                if (amount.equals("")) {
                    amountET.setError("Input Amount.");
                } else if (percentage.equals("")) {
                    amountET.setError("Input percentage.");
                }  else if (people.equals("")) {
                    peopleET.setError("Total Person.");
                }
                else {
                    double finalAmount = ((Double.valueOf(amount) / 100) * (Double.valueOf(percentage)/100)) * 100;

                    PerPersonTV.setText(String.valueOf(finalAmount));
                    double totalAmount = finalAmount*Double.valueOf(people);
                    amountTV.setText(String.valueOf(totalAmount));
                }
            }
        });
    }
}