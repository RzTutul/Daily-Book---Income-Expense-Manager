package com.rztechtunes.dailyexpensemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;

public class DiaryDetailsFrag extends Fragment {
    public static long diaryID;
    private BottomSheetBehavior mBottomSheetBehavior;
    ImageView backIV, moodImageView;
    TextView tittleTV, noteTV, dateTV, moodTV;
    FloatingActionButton shareBtn;
    String mood;
    private InterstitialAd mInterstitialAd;
    public DiaryDetailsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        backIV = view.findViewById(R.id.backIV);
        moodImageView = view.findViewById(R.id.moodImageView);
        tittleTV = view.findViewById(R.id.tittleTV);
        noteTV = view.findViewById(R.id.noteTV);
        moodTV = view.findViewById(R.id.moodTV);
        dateTV = view.findViewById(R.id.dateTV);
        shareBtn = view.findViewById(R.id.shareBtn);


        //Interstitial Add Run
        MobileAds.initialize(getActivity(),getString(R.string.appid));
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.dairy_detials_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.dailyNoteFragment);
            }
        });

        final ImageView buttonExpand = view.findViewById(R.id.button_expand);

        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    // buttonExpand.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //  buttonExpand.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                }

            }
        });


        final DairyPojo dairyPojo = ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().getDairyByID(diaryID);
        mood = dairyPojo.getMood();
        tittleTV.setText(dairyPojo.getTitle());
        noteTV.setText(dairyPojo.getNote());
        dateTV.setText(dairyPojo.getDate());
        moodTV.setText("Felling " + mood);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.layout_animation);
        noteTV.startAnimation(animation);

        if (("Happy").equals(dairyPojo.getMood())) {
            moodImageView.setImageResource(R.drawable.ic_happy);
        } else if (("Sad").equals(dairyPojo.getMood())) {
            moodImageView.setImageResource(R.drawable.ic_unhappy);
        } else if (("Love").equals(dairyPojo.getMood())) {
            moodImageView.setImageResource(R.drawable.ic_in_love);
        } else if (("Sick").equals(dairyPojo.getMood())) {
            moodImageView.setImageResource(R.drawable.ic_ill);
        } else if (("Surprised").equals(dairyPojo.getMood())) {
            moodImageView.setImageResource(R.drawable.ic_surprised);
        } else if (("Angry").equals(dairyPojo.getMood())) {
            moodImageView.setImageResource(R.drawable.ic_mad);
        } else if (("Funny").equals(dairyPojo.getMood())) {
            moodImageView.setImageResource(R.drawable.ic_tongue_out);
        }


        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }



                String title = dairyPojo.getTitle();
                String note = dairyPojo.getNote();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + note);
                getActivity().startActivity(Intent.createChooser(shareIntent, "Share Diary"));
            }
        });


    }
}