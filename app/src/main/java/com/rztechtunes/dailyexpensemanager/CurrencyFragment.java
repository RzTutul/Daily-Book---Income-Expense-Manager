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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CurrencyFragment extends Fragment {
    private static DecimalFormat df2 = new DecimalFormat("#.####");
    EditText currencyET;
    TextView currencyResultTV, currencyTV;
    Spinner fromSP, toSP;
    Button calcuateBtn;

    String SelctedfromCounty, SelectedToCountry;
    double convertValue;

    String fromCountyName[] = {"USD", "EUR"};
    String fromDolarName[] = {"US Dollar", "Euro"};
    String toCountryName[] = {"BDT", "PKR", "INR","SGD", "MYR",  "CYN"};
    String toDolarName[] = {"Bangladeshi Taka", "Pakistani Rupee", "Indian Rupee", "Singapore Dollar", "Malaysian Ringgit", "Chinese Yuan"};
    int fromCountryImage[] = {R.drawable.usa_image, R.drawable.euro_image};
    int toCountyImages[] = {R.drawable.bangldesh_image, R.drawable.pakistan_images, R.drawable.india_image, R.drawable.singapure_images, R.drawable.malaysiaimages, R.drawable.china_image};

    List<CounrtySPPojo> toCountrylist = new ArrayList<>();
    List<CounrtySPPojo> fromCountrylist = new ArrayList<>();

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
        currencyTV = view.findViewById(R.id.currencyTV);


        for (int i = 0; i < fromCountyName.length; i++) {
            fromCountrylist.add(new CounrtySPPojo(fromCountyName[i], fromDolarName[i], fromCountryImage[i]));
        }

          for (int i = 0; i < toCountryName.length; i++) {
            toCountrylist.add(new CounrtySPPojo(toCountryName[i], toDolarName[i], toCountyImages[i]));
        }

        CustomSpinnerAdapter fromCustomSpinnerAdapter = new CustomSpinnerAdapter(getContext(), fromCountrylist);
        CustomSpinnerAdapter toCustomSpinnerAdapter = new CustomSpinnerAdapter(getContext(), toCountrylist);

        fromSP.setAdapter(fromCustomSpinnerAdapter);
        toSP.setAdapter(toCustomSpinnerAdapter);

        fromSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SelctedfromCounty = fromCountrylist.get(position).getCountyName();
                Toast.makeText(getActivity(), "" + fromCountrylist.get(position).getCountyName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        toSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedToCountry = toCountrylist.get(position).getCountyName();
                Toast.makeText(getActivity(), "" + toCountrylist.get(position).getCountyName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calcuateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertValue =0.0;
                String amount = currencyET.getText().toString();

                if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("BDT")) {
                    convertValue = Double.parseDouble(amount) * 84.9250;
                    currencyResultTV.setText("≈ " + (df2.format(convertValue)) + " BDT");
                    currencyTV.setText("1 USD to ≈84.9250 BDT");
                }
                else if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("PKR")) {
                    convertValue = Double.parseDouble(amount) * 166.938;
                    currencyResultTV.setText("≈ " + (df2.format(convertValue))  + " PKR");
                    currencyTV.setText("1 USD to ≈166.938 PKR");
                }
                   else if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("INR")) {
                    convertValue = Double.parseDouble(amount) * 74.6562;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " INR");
                    currencyTV.setText("1 USD to ≈74.6562 INR");
                }
                   else if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("SGD")) {
                    convertValue = Double.parseDouble(amount) * 1.39500;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " SGD");
                    currencyTV.setText("1 USD to ≈1.39500 SGD");
                }  else if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("MYR")) {
                    convertValue = Double.parseDouble(amount) * 4.28670;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " MYR");
                    currencyTV.setText("1 USD to ≈4.28670 MYR");
                } else if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("CNY")) {
                    convertValue = Double.parseDouble(amount) *7.06617;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " CNY");
                    currencyTV.setText("1 USD to ≈7.06617 CNY");
                }

                   //EURO
               else if (SelctedfromCounty.equals("EUR") & SelectedToCountry.equals("BDT")) {
                    convertValue = Double.parseDouble(amount) * 95.5166;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " BDT");
                    currencyTV.setText("1 EUR to ≈95.5166 BDT");
                }
                else if (SelctedfromCounty.equals("EUR") & SelectedToCountry.equals("PKR")) {
                    convertValue = Double.parseDouble(amount) * 187.758;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " PKR");
                    currencyTV.setText("1 EUR to ≈166.938 PKR");
                }
                   else if (SelctedfromCounty.equals("EUR") & SelectedToCountry.equals("INR")) {
                    convertValue = Double.parseDouble(amount) * 83.9577;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " INR");
                    currencyTV.setText("1 EUR to ≈83.9577 INR");
                }
                   else if (SelctedfromCounty.equals("EUR") & SelectedToCountry.equals("SGD")) {
                    convertValue = Double.parseDouble(amount) * 1.56897;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " SGD");
                    currencyTV.setText("1 EUR to ≈1.56897 SGD");
                }  else if (SelctedfromCounty.equals("EUR") & SelectedToCountry.equals("MYR")) {
                    convertValue = Double.parseDouble(amount) * 4.82131;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " MYR");
                    currencyTV.setText("1 EUR to ≈4.82131 MYR");
                } else if (SelctedfromCounty.equals("USD") & SelectedToCountry.equals("CNY")) {
                    convertValue = Double.parseDouble(amount) *7.94736;
                    currencyResultTV.setText("≈ " +(df2.format(convertValue))  + " CNY");
                    currencyTV.setText("1 USD to ≈7.94736 CNY");
                }


            }
        });


    }
}