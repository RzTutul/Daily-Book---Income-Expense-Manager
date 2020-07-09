package com.rztechtunes.dailyexpensemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

public class SettingFragment extends Fragment {

SwitchCompat patternOnOfSW;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        patternOnOfSW = view.findViewById(R.id.swOnOff);

        userActivityStorePref = new UserActivityStorePref(getContext());
        boolean switchStatus = userActivityStorePref.getPatterSwithStatus();
        if (switchStatus)
        {
            patternOnOfSW.setChecked(true);
        }

        patternOnOfSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (patternOnOfSW.isChecked())
                {
                    userActivityStorePref.setPatterActivity(true);
                    userActivityStorePref.setPatternSwithStatus(true);
                    Intent intent = new Intent(getActivity(), PatternLockActivity.class);
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getContext(), "off", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}