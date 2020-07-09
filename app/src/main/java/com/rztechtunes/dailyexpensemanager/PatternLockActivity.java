package com.rztechtunes.dailyexpensemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PatternLockActivity extends AppCompatActivity implements PatternLockViewListener {

    PatternLockView mpatternLockView;
    boolean isLuch;
    String finalKey, firstPatternKey, storeName;
    UserActivityStorePref userActivityStorePref;
    Button savePatnBatn;
    TextView patternTV, forgetPattBtn;
    TextInputEditText personET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);

        mpatternLockView = findViewById(R.id.pattern_lock_view);
        savePatnBatn = findViewById(R.id.saveBtn);
        patternTV = findViewById(R.id.drawTV);
        personET = findViewById(R.id.personET);
        forgetPattBtn = findViewById(R.id.forgotPatternBtn);
        mpatternLockView.addPatternLockListener(this);
        userActivityStorePref = new UserActivityStorePref(this);
        isLuch = userActivityStorePref.getPatternActivity();
        finalKey = userActivityStorePref.getPatternKey();
        storeName = userActivityStorePref.getName();


        if (isLuch) {
            forgetPattBtn.setVisibility(View.VISIBLE);
            if (finalKey.equals("543543210")) {
                forgetPattBtn.setVisibility(View.GONE);
                savePatnBatn.setVisibility(View.VISIBLE);
                personET.setVisibility(View.VISIBLE);
                patternTV.setText("Set Your Pattern & Security");
            }
        } else {
            gotoMainActivity();
        }


        savePatnBatn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String personName = personET.getText().toString();
                if (personName.equals("")) {
                    personET.setError("Enter a person name");
                } else {
                    if (storeName.equals("tutulxcvd")) {
                        userActivityStorePref.setPatterKey(firstPatternKey);
                        userActivityStorePref.setPatterActivity(true);
                        userActivityStorePref.setPersonName(personName);
                        Toast.makeText(PatternLockActivity.this, "Pattern Saved", Toast.LENGTH_SHORT).show();
                        gotoMainActivity();
                    } else {
                        if (storeName.equals(personName)) {
                            userActivityStorePref.setPatterKey(firstPatternKey);
                            userActivityStorePref.setPatterActivity(true);
                            userActivityStorePref.setPersonName(personName);
                            Toast.makeText(PatternLockActivity.this, "Pattern Saved", Toast.LENGTH_SHORT).show();
                            gotoMainActivity();
                        } else {
                            Snackbar.make(v, "Favorite Person name didn't matched!", Snackbar.LENGTH_LONG).show();
                        }
                    }


                }


            }
        });

        forgetPattBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(PatternLockActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Your set Pattern will be reset!")
                        .setConfirmText("Yes,reset it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                personET.setVisibility(View.VISIBLE);
                                patternTV.setText("Set New Pattern");
                                savePatnBatn.setVisibility(View.VISIBLE);
                                userActivityStorePref.setPatterKey("543543210");
                                finalKey = userActivityStorePref.getPatternKey();
                                sDialog.dismissWithAnimation();
                                //   Navigation.findNavController(holder.itemView).navigate(R.id.expenseManagerFrag);

                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();


            }
        });

    }

    private void gotoMainActivity() {

        Intent intent = new Intent(PatternLockActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {
    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {

        if (finalKey.equals("543543210")) {
            firstPatternKey = PatternLockUtils.patternToString(mpatternLockView, pattern);

        }
        else {
            String currentPattern = PatternLockUtils.patternToString(mpatternLockView, pattern);
            if (finalKey.equalsIgnoreCase(currentPattern)) {
                mpatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                gotoMainActivity();
            } else {
                mpatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                Toast.makeText(this, "Incorrect Pattern!", Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    public void onCleared() {

    }


}