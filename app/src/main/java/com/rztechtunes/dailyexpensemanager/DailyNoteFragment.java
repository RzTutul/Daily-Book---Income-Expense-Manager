package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.rztechtunes.dailyexpensemanager.adapter.DairyRVAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;

import java.util.ArrayList;
import java.util.List;

public class DailyNoteFragment extends Fragment {

    RecyclerView dairyRV;
    ExtendedFloatingActionButton addDairyBtn;
    List<DairyPojo> dairyPojoList = new ArrayList<>();

    public DailyNoteFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dairyRV = view.findViewById(R.id.dairyRV);
        addDairyBtn = view.findViewById(R.id.addDairyBtn);

        addDairyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.addDairyFragment);
            }
        });

        dairyPojoList = ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().getAllDairyList();


        DairyRVAdapter dairyRVAdapter = new DairyRVAdapter(getActivity(), dairyPojoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        dairyRV.setLayoutManager(llm);
        dairyRV.setAdapter(dairyRVAdapter);

        dairyRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        addDairyBtn.show();
                        break;
                    default:
                        addDairyBtn.hide();
                        break;
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });


    }
}