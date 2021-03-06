package com.rztechtunes.dailyexpensemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;
import com.rztechtunes.dailyexpensemanager.helper.NotificationWorker;
import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

import java.util.concurrent.TimeUnit;

public class SettingFragment extends Fragment {

    SwitchCompat patternOnOfSW, splashOnOfSW;
    LinearLayout addCatagoriesLL, addCataBOxLL, addPersonLL, addPersonBoxLL, inforLL, infoBoxLL;
    Button saveCataBtn, savePersonBtn;
    EditText personET, categoiresET;
    CardView shareCV;

    LinearLayout addCurrencyLL, currencyLL;
    RadioGroup currencyRadioGrp;
    RadioButton usdRB,bdRB,euroRB,inrRB;
    Button currencySaveBtn;
    String selectedCurrency;

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
        shareCV = view.findViewById(R.id.shareCV);
        addCurrencyLL = view.findViewById(R.id.AddCurrencyLL);
        currencyLL = view.findViewById(R.id.CurrencyLL);
        currencyLL = view.findViewById(R.id.CurrencyLL);
        currencyRadioGrp = view.findViewById(R.id.currencyRadioGroup);
        usdRB = view.findViewById(R.id.usdRadio);
        bdRB = view.findViewById(R.id.bdRadio);
        euroRB = view.findViewById(R.id.eurRadio);
        inrRB = view.findViewById(R.id.inrRadio);
        currencySaveBtn = view.findViewById(R.id.currencySaveBtn);

        shareCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "DailyBook");
                    String shareMessage = "\nDailyBook\nDaily,Monthly,Yearly expense-income tracker\nDaily Calculation\nDiary Note\nInstall this cool App.\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        userActivityStorePref = new UserActivityStorePref(getActivity());
        boolean notificationStatus = userActivityStorePref.getNotification();
        boolean runNotificaiotn = userActivityStorePref.getNotificationStatus();

        if (runNotificaiotn) {

        } else {
            ///Send Notification
            Constraints constraints =
                    new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();

            PeriodicWorkRequest periodicWorkRequest =
                    new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                            .setConstraints(constraints).build();
            WorkManager.getInstance(getActivity())
                    .enqueue(periodicWorkRequest);
            userActivityStorePref.setOnTimeNotificaiton(true);
        }


        if (notificationStatus) {
            WorkManager.getInstance().cancelAllWorkByTag("notification");
        }

        addCatagoriesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCataBOxLL.getVisibility() == View.VISIBLE) {
                    addCataBOxLL.setVisibility(View.GONE);

                } else {
                    addCataBOxLL.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.layout_animation);
                    addCataBOxLL.startAnimation(animation);
                }
            }
        });

       String currencySymbol= userActivityStorePref.getCurrency();
        if (("$").equals(currencySymbol)) {
            usdRB.setChecked(true);
        } else if (("৳").equals(currencySymbol)) {
            bdRB.setChecked(true);
        } else if (("₹").equals(currencySymbol)) {
            inrRB.setChecked(true);
        } else if (("€").equals(currencySymbol)) {
            euroRB.setChecked(true);
        }

        currencySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = currencyRadioGrp.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(selectedID);
                String selectedRadio = (String) radioButton.getText();
                if (("USD - $").equals(selectedRadio)) {
                    selectedCurrency = "$";
                } else if (("BD - ৳").equals(selectedRadio)) {
                    selectedCurrency = "৳";
                } else if (("INR - ₹").equals(selectedRadio)) {
                    selectedCurrency = "₹";
                } else if (("EUR - €").equals(selectedRadio)) {
                    selectedCurrency = "€";
                }
                userActivityStorePref.setCurrency(selectedCurrency);
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        addCurrencyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currencyLL.getVisibility() == View.VISIBLE) {
                    currencyLL.setVisibility(View.GONE);

                } else {
                    currencyLL.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.layout_animation);
                    currencyLL.startAnimation(animation);
                }
            }
        });


        saveCataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cataName = categoiresET.getText().toString();
                if (cataName.equals("")) {
                    categoiresET.setError("Add New Categories");
                } else {
                    CategoriesPojo categoriesPojo = new CategoriesPojo(cataName);
                    long insert = ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().InsertCatagoires(categoriesPojo);
                    if (insert > 0) {
                        Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                    }
                    categoiresET.setText("");
                }
            }
        });

        addPersonLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userActivityStorePref.getName();
                View parentView = view.findViewById(android.R.id.content);
                if (name.equals("tutulxcvd")) {
                    Snackbar snackbar = Snackbar.make(view, "Firstly, Turn on Pattern Lock", Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(addCatagoriesLL);
                    snackbar.show();
                } else {
                    if (addPersonBoxLL.getVisibility() == View.VISIBLE) {
                        addPersonBoxLL.setVisibility(View.GONE);
                    } else {
                        addPersonBoxLL.setVisibility(View.VISIBLE);
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.layout_animation);
                        addPersonBoxLL.startAnimation(animation);
                    }
                }
            }
        });

        savePersonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = personET.getText().toString();
                if (name.equals("")) {
                    personET.setError("Favorite Person Name");

                } else {
                    userActivityStorePref.setPersonName(name);
                    Snackbar snackbar = Snackbar.make(view, "Saved Successfully", Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(splashOnOfSW);
                    snackbar.show();

                }
            }
        });

        inforLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (infoBoxLL.getVisibility() == View.VISIBLE) {
                    infoBoxLL.setVisibility(View.GONE);
                } else {
                    infoBoxLL.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.layout_animation);
                    infoBoxLL.startAnimation(animation);

                }
            }
        });


        userActivityStorePref = new UserActivityStorePref(getContext());
        boolean switchStatus = userActivityStorePref.getPatterSwithStatus();
        if (switchStatus) {
            patternOnOfSW.setChecked(true);
        } else {
            patternOnOfSW.setChecked(false);

        }


        //FirstTime Turn ON Splash Screen
        boolean splashStatus = userActivityStorePref.getNotification();
        if (splashStatus) {
            splashOnOfSW.setChecked(false);

        } else {
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

                if (splashOnOfSW.isChecked()) {
                    splashOnOfSW.setChecked(true);
                    userActivityStorePref.setNotfication(false);

                } else {
                    splashOnOfSW.setChecked(false);
                    userActivityStorePref.setNotfication(true);
                    Toast.makeText(getActivity(), "Turn Off Notification", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}