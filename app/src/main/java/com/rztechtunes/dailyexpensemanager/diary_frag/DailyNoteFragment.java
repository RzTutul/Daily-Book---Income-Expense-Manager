package com.rztechtunes.dailyexpensemanager.diary_frag;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rztechtunes.dailyexpensemanager.DiaryDetailsFrag;
import com.rztechtunes.dailyexpensemanager.R;
import com.rztechtunes.dailyexpensemanager.adapter.DairyRVAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.helper.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DailyNoteFragment extends Fragment {

    CardView emptyCV;
    RecyclerView dairyRV;
    FloatingActionButton addDairyBtn;
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


        AppBarLayout mAppBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                  //  showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                   // hideOption(R.id.action_info);
                }
            }
        });








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



        dairyRV.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), dairyRV, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {


                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
                        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_diary_operation, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));


                        TextView titleTV = bottomSheetView.findViewById(R.id.titleTV);

                        titleTV.setText(dairyPojoList.get(position).getTitle());

                        bottomSheetView.findViewById(R.id.editLL).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Bundle bundle = new Bundle();
                                bundle.putLong("dairyID", dairyPojoList.get(position).getDairyid());
                                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.addDairyFragment, bundle);

                                bottomSheetDialog.dismiss();


                            }
                        });
                        bottomSheetView.findViewById(R.id.deleteLL).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Are you sure?")
                                        .setContentText("Won't be able to recover this file!")
                                        .setConfirmText("Yes,delete it!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                try {
                                                    try {
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

                                bottomSheetDialog.dismiss();
                            }
                        });
                        bottomSheetView.findViewById(R.id.readLL).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DiaryDetailsFrag.diaryID = dairyPojoList.get(position).getDairyid();
                                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.diaryDetailsFrag);

                                bottomSheetDialog.dismiss();
                            }
                        });


                        bottomSheetDialog.setContentView(bottomSheetView);
                        bottomSheetDialog.show();



                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );




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