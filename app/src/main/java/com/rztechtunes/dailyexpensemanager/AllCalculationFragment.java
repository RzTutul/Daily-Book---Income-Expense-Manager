package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AllCalculationFragment extends Fragment {

    CardView percentageCard, tipCard, currencyCard, bmiCard, perUnitCard, loanCard, interstCard, smokedCard;
    CardView ageCard,dateCard,timeCard,wagesCard;

    public AllCalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_calculation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        percentageCard = view.findViewById(R.id.percentageCard);
        tipCard = view.findViewById(R.id.tipCard);
        currencyCard = view.findViewById(R.id.currencyCard);
        bmiCard = view.findViewById(R.id.bmiCard);
        perUnitCard = view.findViewById(R.id.perUnitCard);
        loanCard = view.findViewById(R.id.loanCard);
        interstCard = view.findViewById(R.id.interstCard);
        smokedCard = view.findViewById(R.id.smokedCard);
        ageCard = view.findViewById(R.id.ageCard);
        dateCard = view.findViewById(R.id.dateCard);
        timeCard = view.findViewById(R.id.timeCard);
        wagesCard = view.findViewById(R.id.wagesCard);

        percentageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.percentageFragment);
            }
        });

        tipCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.tipsFragment);

            }
        });

        currencyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.currencyFragment);

            }
        });
        bmiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.calculateIBMFragment);

            }
        });
        perUnitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.perUnitFragment);

            }
        });

        loanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.loanFragment);

            }
        });
        interstCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.interestFragment);

            }
        });

        smokedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.smokingCostFragment);

            }
        });
        ageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.ageCalculationFragment);

            }
        });  dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.dateDifferentFragment);

            }
        });  timeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.timeDifferenceFragment);

            }
        });
        wagesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.wagesCalculationFragment);

            }
        });
    }
}