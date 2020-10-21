package com.rztechtunes.dailyexpensemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.adapter.ExpenseIncomeAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;
import com.rztechtunes.dailyexpensemanager.helper.CSVWriter;
import com.rztechtunes.dailyexpensemanager.helper.Utils;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class ExpenseManagerFrag extends Fragment {
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private AdView mAdView;

    LinearLayout pieCharBtnLL;
    CardView emptyCV;
    ImageView fillterData;
    private String exCatagories;
    private String e_date;
    RecyclerView expenseRV;
    List<ExpenseIncomePojo> expenseIncomePojos = new ArrayList<>();
    List<String> categoriesPojoList = new ArrayList<>();
    TextView monthNameTV;
    TickerView incomeTV, expenseTV, balanceTV;
    ExpenseIncomeAdapter expenseIncomeAdapter;
    String select_month, select_year;
    List<String> monthName = new ArrayList<>();
    List<String> year = new ArrayList<>();
    UserActivityStorePref userActivityStorePref;
    public ExpenseManagerFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        expenseRV = view.findViewById(R.id.expenseRV);
        incomeTV = view.findViewById(R.id.incomeTV);
        expenseTV = view.findViewById(R.id.expenesTV);
        balanceTV = view.findViewById(R.id.balanceTV);
        emptyCV = view.findViewById(R.id.emptyCardView);
        monthNameTV = view.findViewById(R.id.monthNameTV);
        fillterData = view.findViewById(R.id.fillterData);
        userActivityStorePref = new UserActivityStorePref(getActivity());
       //   = view.findViewById(R.id.pieCharBtnLL);

        //For TextViewCountAnimation
        incomeTV.setCharacterLists(TickerUtils.provideNumberList());
        expenseTV.setCharacterLists(TickerUtils.provideNumberList());
        balanceTV.setCharacterLists(TickerUtils.provideNumberList());

        select_month = Utils.getMonthName();
        select_year = Utils.getYear();
        monthNameTV.setText(select_month);


//        title.setTitle(Utils.getMonthName());

        //Banner Add
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        emptyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Navigation.findNavController(v).navigate(R.id.addTransactionFrag);
            }
        });




        //Method for query all expense Data and show RV
        featchData();
        fillterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFillterData();
            }
        });

    }

    private void getFillterData() {
        monthName = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDistinctMonthName();
        year = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDistanctYear();

        if (monthName.size()==0)
        {
            new SweetAlertDialog(getActivity())
                    .setTitleText("Database is empty!")
                    .setContentText("Firstly, Insert your transaction")
                    .show();

            Toast.makeText(getActivity(), "Year & Month is empty!", Toast.LENGTH_LONG).show();
        }

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_search_expense, (LinearLayout) getActivity().findViewById(R.id.bottomSheetContainer));

        Spinner monthSP = bottomSheetView.findViewById(R.id.selectMonthSP);
        Spinner yearSP = bottomSheetView.findViewById(R.id.selectYearSP);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, monthName);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, year);
        monthSP.setAdapter(monthAdapter);
        yearSP.setAdapter(yearAdapter);
        monthSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_month = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bottomSheetView.findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    featchData();
                    monthNameTV.setText(select_month);
                    bottomSheetDialog.dismiss();



            }
        });
        bottomSheetView.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStorageRequestAccepted())
                {

                    saveCSVfile();
                }

                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void featchData() {
        double t_income = 0.0, t_expense = 0.0, t_balance = 0.0;

        String currencySymbol = userActivityStorePref.getCurrency();

        Log.i(TAG, "featchData: "+Utils.getMonthName()+" "+Utils.getYear());
        expenseIncomePojos = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(select_month, select_year);

        if (expenseIncomePojos.size() == 0) {
            emptyCV.setVisibility(View.VISIBLE);

            expenseIncomeAdapter = new ExpenseIncomeAdapter(getActivity(), expenseIncomePojos);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            expenseRV.setLayoutManager(llm);
            expenseRV.setAdapter(expenseIncomeAdapter);

            incomeTV.setText(currencySymbol+decimalFormat.format(t_income));
            expenseTV.setText("-"+currencySymbol+decimalFormat.format(t_expense));
            balanceTV.setText(currencySymbol+decimalFormat.format(t_balance));


        } else {

            emptyCV.setVisibility(View.GONE);
            for (ExpenseIncomePojo expenseIncomePojo : expenseIncomePojos) {
                String catagories = expenseIncomePojo.getE_catagories();
                if (catagories.equals("Income")) {
                    t_income = t_income + Double.valueOf(expenseIncomePojo.getE_amount());
                } else {
                    t_expense = t_expense + Double.valueOf(expenseIncomePojo.getE_amount());
                }

            }
            t_balance = t_income - t_expense;


            incomeTV.setText(currencySymbol+decimalFormat.format(t_income));
            expenseTV.setText("-"+currencySymbol+decimalFormat.format(t_expense));
            balanceTV.setText(currencySymbol+decimalFormat.format(t_balance));


            expenseIncomeAdapter = new ExpenseIncomeAdapter(getActivity(), expenseIncomePojos);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            expenseRV.setLayoutManager(llm);
            expenseRV.setAdapter(expenseIncomeAdapter);



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
                            .setContentText("Won't be able to recover this!")
                            .setConfirmText("Yes,delete it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    try {


                                        try {

                                            int position = viewHolder.getAdapterPosition();
                                            // dairyPojos.remove(position);
                                            ExpenseIncomePojo expenseIncomePojo = expenseIncomePojos.get(position);
                                            ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().deleteExIncome(expenseIncomePojo);
                                            expenseIncomeAdapter.notifyDataSetChanged();
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
                                    expenseIncomeAdapter.notifyDataSetChanged();
                                }
                            })
                            .show();

                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(expenseRV);


        }




    }

    private void saveCSVfile() {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "DailyBook");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "expenseIncome"+select_month+"_"+select_year+".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            ExpenseIncomeDatabase db = ExpenseIncomeDatabase.getInstance(getContext());

           // Cursor curCSV = db.query("SELECT * FROM " + "ExpenseIncomeTbl", null); use for when get all data from database

            Cursor curCSV = db.query("SELECT * FROM ExpenseIncomeTbl Where e_month = ? and e_year = ?",new String[]{select_month,select_year});
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i < curCSV.getColumnCount(); i++)
                    arrStr[i] = curCSV.getString(i);
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            Toast.makeText(getActivity(), "Exported", Toast.LENGTH_SHORT).show();

            // 1. Success message
            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("CSV file generated!")
                    .setContentText("Path: Internal Storage/DailyBook/expenseIncome.csv")
                    .show();






        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }


    private boolean isStorageRequestAccepted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionList = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissionList, 123);
                return false;
            }
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {


            case 123: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveCSVfile();

                } else {
                    Toast.makeText(getActivity(), "Permission deny", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_piechart:
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.reportGraphFrag);
                break;
                case R.id.menu_history:
                    getFillterData();
                break;

        }
        return super.onOptionsItemSelected(item);


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_option, menu);
    }
}