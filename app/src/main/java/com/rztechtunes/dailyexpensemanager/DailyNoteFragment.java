package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.rztechtunes.dailyexpensemanager.adapter.DairyRVAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DailyNoteFragment extends Fragment {

    CardView emptyCV;
    RecyclerView dairyRV;
    ExtendedFloatingActionButton addDairyBtn;
    List<DairyPojo> dairyPojoList = new ArrayList<>();
    DairyRVAdapter dairyRVAdapter;

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
        emptyCV = view.findViewById(R.id.emptyCardView);


        emptyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.addDairyFragment);
            }
        });

        addDairyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.addDairyFragment);
            }
        });

        dairyPojoList = ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().getAllDairyList();

        if (dairyPojoList.size() == 0) {
            emptyCV.setVisibility(View.VISIBLE);
            featchData();
        } else {
            emptyCV.setVisibility(View.GONE);
            featchData();
        }


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

        //Swipe To delete expenseRow
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //Toast.makeText(getActivity(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                try {


                                    try {

                                        int position = viewHolder.getAdapterPosition();
                                        // dairyPojos.remove(position);
                                        DairyPojo dairyPojo = dairyPojoList.get(position);
                                        ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().deleteDairy(dairyPojo);
                                        dairyRVAdapter.notifyDataSetChanged();
                                        featchData();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                sDialog.dismissWithAnimation();


                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                dairyRVAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(dairyRV);


    }

    private void featchData() {
        dairyPojoList = ExpenseIncomeDatabase.getInstance(getContext()).getDairyDao().getAllDairyList();
        dairyRVAdapter = new DairyRVAdapter(getActivity(), dairyPojoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        dairyRV.setLayoutManager(llm);
        dairyRV.setAdapter(dairyRVAdapter);
    }
}