package com.rztechtunes.dailyexpensemanager.diary_frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.rztechtunes.dailyexpensemanager.R;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.helper.Utils;

public class AddDairyFragment extends Fragment {
    NestedScrollView nestedScrollView;
    TextView dateTV,moodTV;
    EditText titleET;
    EditText dairyNoteET;
    Button saveBtn;
    Button updateBtn;
    String eventID;
    String date;
    long dairyID;
    Toolbar toolbar;
    ImageView happyImageView,sadImageView,loveImageView,owoImageView,sickImageVIew,angreyImageView,funnyImageView;
    private AdView mAdView;
    String mood = "Happy";

    public AddDairyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            dairyID = bundle.getLong("dairyID");

        }


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_dairy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dateTV = view.findViewById(R.id.d_dateTV);
        titleET = view.findViewById(R.id.d_titleET);
        dairyNoteET = view.findViewById(R.id.d_noteET);
        saveBtn = view.findViewById(R.id.d_savebtn);
        updateBtn = view.findViewById(R.id.d_updatebtn);
        nestedScrollView = view.findViewById(R.id.nestedView);
        happyImageView = view.findViewById(R.id.happyImageView);
        sadImageView = view.findViewById(R.id.sadImageView);
        loveImageView = view.findViewById(R.id.loveImageVIew);
        sickImageVIew = view.findViewById(R.id.sickImageView);
        owoImageView = view.findViewById(R.id.owoImageView);
        angreyImageView = view.findViewById(R.id.angreyImageView);
        funnyImageView = view.findViewById(R.id.funnyImageView);
        toolbar = view.findViewById(R.id.toolbar);
        moodTV = view.findViewById(R.id.moodTV);

        nestedScrollView = view.findViewById(R.id.nestedView);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.dailyNoteFragment);
            }
        });

        happyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = "Happy";
                moodTV.setText(mood);
            }
        });
            sadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = "Sad";
                moodTV.setText(mood);
            }
        });
            loveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = "Love";
                moodTV.setText(mood);
            }
        });
            sickImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = "Sick";
                moodTV.setText(mood);
            }
        });
            owoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = "Surprised";
                moodTV.setText(mood);
            }
        });
            angreyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = "Angry";
                moodTV.setText(mood);
            }
        });
            funnyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = "Funny";
                moodTV.setText(mood);
            }
        });



        //Banner Add
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dairyNoteET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        //For Scrolling up text in EditBox
        dairyNoteET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.d_noteET) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });



        date = Utils.getCurrentDateWithDay();
        String[] dateValue = date.split("\\s+");
        dateTV.setText(dateValue[0]);


        if (dairyID > 0) {
            updateBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
        }

        if (dairyID > 0) {
            DairyPojo dairyPojo = ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().getDairyByID(dairyID);
            dateTV.setText(dairyPojo.getDate());
            titleET.setText(dairyPojo.getTitle());
            dairyNoteET.setText(dairyPojo.getNote());
            moodTV.setText(dairyPojo.getMood());
            mood = dairyPojo.getMood();
        }


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date  = dateTV.getText().toString();
                String title = titleET.getText().toString().trim();
                String note  = dairyNoteET.getText().toString() ;
                if (title.equals("")) {
                    titleET.setError("Give Title");
                } else if (note.isEmpty()) {
                    dairyNoteET.setError("Write Something!");
                } else {
                    DairyPojo dairyPojo = new DairyPojo(date, title, note,mood);
                    long insert = ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().inserDairy(dairyPojo);
                    if (insert > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", eventID);
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.dailyNoteFragment, bundle);
                        Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Insert Fail", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateTV.getText().toString();
                String title = titleET.getText().toString();
                String note = dairyNoteET.getText().toString();

                if (title.equals("")) {
                    titleET.setError("Give Title");
                } else if (note.isEmpty()) {
                    dairyNoteET.setError("Write Something!");
                } else {
                    
                    DairyPojo dairyPojo = new DairyPojo(dairyID, date, title, note,mood);
                    int update = ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().updateDairy(dairyPojo);
                    if (update > 0) {
                        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.dailyNoteFragment);
                    }


                }
            }
        });


    }
}