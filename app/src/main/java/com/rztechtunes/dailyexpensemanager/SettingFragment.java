package com.rztechtunes.dailyexpensemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;
import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

public class SettingFragment extends Fragment {

    SwitchCompat patternOnOfSW,splashOnOfSW;
    LinearLayout addCatagoriesLL,addCataBOxLL,addPersonLL,addPersonBoxLL,inforLL,infoBoxLL;
    Button saveCataBtn,savePersonBtn;
    EditText personET,categoiresET;

    public SettingFragment() {
        // Required empty public constructor
    }

    UserActivityStorePref userActivityStorePref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patternOnOfSW = view.findViewById(R.id.swOnOff);
        splashOnOfSW = view.findViewById(R.id.splashOnOff);
        addCatagoriesLL = view.findViewById(R.id.addCatagoreisLL);
        addCataBOxLL = view.findViewById(R.id.addCataBOxLL);
        categoiresET = view.findViewById(R.id.categoiresET);
        saveCataBtn = view.findViewById(R.id.saveCategoriesBtn);
        addPersonLL = view.findViewById(R.id.addPersonLL);
        addPersonBoxLL = view.findViewById(R.id.addPersonBOxLL);
        personET = view.findViewById(R.id.personET);
        savePersonBtn = view.findViewById(R.id.savePersonBtn);
        inforLL = view.findViewById(R.id.infoLL);
        infoBoxLL = view.findViewById(R.id.infoBoxLL);


        addCatagoriesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCataBOxLL.getVisibility()==View.VISIBLE)
                {
                    addCataBOxLL.setVisibility(View.GONE);

                }
                else
                {
                   addCataBOxLL.setVisibility(View.VISIBLE);
                }
            }
        });

        saveCataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cataName = categoiresET.getText().toString();
                if (cataName.equals(""))
                {
                    categoiresET.setError("Add New Categories");
                }
                else
                {
                    CategoriesPojo categoriesPojo = new CategoriesPojo(cataName);
                    long insert = ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().InsertCatagoires(categoriesPojo);
                    if (insert>0)
                    {
                        Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        addPersonLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name = userActivityStorePref.getName();
               View parentView = view.findViewById(android.R.id.content);
               if (name.equals("tutulxcvd"))
               {
                   Snackbar snackbar = Snackbar.make(view,"Firstly, Turn on Pattern Lock",Snackbar.LENGTH_SHORT);
                   snackbar.setAnchorView(addCatagoriesLL);
                   snackbar.show();
               }
               else
               {
                   if (addPersonBoxLL.getVisibility()==View.VISIBLE)
                   {
                       addPersonBoxLL.setVisibility(View.GONE);
                   }
                   else
                   {
                       addPersonBoxLL.setVisibility(View.VISIBLE);

                   }
               }
            }
        });

        savePersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = personET.getText().toString();
                if (name.equals(""))
                {
                    personET.setError("Favorite Person Name");

                }
                else
                {
                    userActivityStorePref.setPersonName(name);
                    Snackbar snackbar  =  Snackbar.make(view,"Saved Successfully",Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(splashOnOfSW);
                    snackbar.show();

                }
            }
        });

        inforLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (infoBoxLL.getVisibility()==View.VISIBLE)
                    {
                        infoBoxLL.setVisibility(View.GONE);
                    }
                    else
                    {
                        infoBoxLL.setVisibility(View.VISIBLE);

                    }
        }});


       userActivityStorePref = new UserActivityStorePref(getContext());
        boolean switchStatus = userActivityStorePref.getPatterSwithStatus();
        if (switchStatus) {
            patternOnOfSW.setChecked(true);
        }
        else
        {
            patternOnOfSW.setChecked(false);

        }


        //FirstTime Turn ON Splash Screen
       boolean splashStatus = userActivityStorePref.getNotification();
        if (splashStatus)
        {
            splashOnOfSW.setChecked(false);

        }
        else
        {
            splashOnOfSW.setChecked(true);

        }

        patternOnOfSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (patternOnOfSW.isChecked()) {
                    userActivityStorePref.setPatternSwithStatus(true);
                    userActivityStorePref.setPatterActivity(true);

                    Intent intent = new Intent(getActivity(), PatternLockActivity.class);
                    startActivity(intent);

                } else {
                    
                    userActivityStorePref.setPatterActivity(false);
                    userActivityStorePref.setPatternSwithStatus(false);
                    Toast.makeText(getContext(), "Pattern Lock is OFF", Toast.LENGTH_SHORT).show();

                }
            }
        });


        splashOnOfSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (splashOnOfSW.isChecked())
                {
                    splashOnOfSW.setChecked(true);
                    userActivityStorePref.setNotfication(false);

                }
                else
                {
                    splashOnOfSW.setChecked(false);
                    userActivityStorePref.setNotfication(true);
                    Toast.makeText(getActivity(), "Turn Off Notification", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}