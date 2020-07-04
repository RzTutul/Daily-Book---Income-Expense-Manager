package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rztechtunes.dailyexpensemanager.adapter.CustomSpinnerAdapter;
import com.rztechtunes.dailyexpensemanager.pojo.CounrtySPPojo;

import java.util.ArrayList;
import java.util.List;

public class CurrencyFragment extends Fragment {
    EditText currencyET;
    TextView currencyResultTV;
    Spinner fromSP, toSP;
    Button calcuateBtn;

    String SelctedfromCounty,SelectedToCountry;
    double convertValue;


    String CountryName[] = {"USD", "BDT", "EUR-Euro", "SGD", "MYR", "PKR", "INR", "CYN"};
    String DolarName[] = {"US Dollar", "Bangladeshi Taka", "Euro", "Singapore Dollar", "Malaysian Ringgit", "Pakistani Rupee", "Indian Rupee", "Chinese Yuan"};
    int countyImages[] = {R.drawable.usa_image, R.drawable.bangldesh_image, R.drawable.euro_image, R.drawable.singapure_images, R.drawable.malaysiaimages, R.drawable.pakistan_images, R.drawable.india_image, R.drawable.china_image};

    List<CounrtySPPojo> list = new ArrayList<>();

    public CurrencyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currencyET = view.findViewById(R.id.currencyAmount);
        currencyResultTV = view.findViewById(R.id.resultTV);
        fromSP = view.findViewById(R.id.fromCountySP);
        toSP = view.findViewById(R.id.toCountrySP);
        calcuateBtn = view.findViewById(R.id.currencyCalcuateBtn);



        for (int i =0;i<CountryName.length;i++)
        {
            list.add(new CounrtySPPojo(CountryName[i],DolarName[i],countyImages[i]));
        }
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(),list);

        fromSP.setAdapter(customSpinnerAdapter);
        toSP.setAdapter(customSpinnerAdapter);

        fromSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SelctedfromCounty = list.get(position).getCountyName();
                Toast.makeText(getActivity(), ""+list.get(position).getCountyName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      toSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedToCountry = list.get(position).getCountyName();
                Toast.makeText(getActivity(), ""+list.get(position).getCountyName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calcuateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = currencyET.getText().toString();

                if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("BDT"))
                {
                    convertValue =Double.valueOf(amount) * 84.9250;
                    currencyResultTV.setText("≈ "+(convertValue)+"\n"+"1 USD to ≈84.9250 BDT");

                }
                if (SelctedfromCounty.equals("BDT") & SelectedToCountry.equals("USD")) {
                    convertValue = Double.valueOf(amount) * (1/84.9250);
                    currencyResultTV.setText("≈ "+(convertValue)+"\n"+"1 BDT to ≈0.0117751 USD");

                }

            }
        });



    }
}