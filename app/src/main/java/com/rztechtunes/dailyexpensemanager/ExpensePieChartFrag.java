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
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.helper.CSVWriter;
import com.rztechtunes.dailyexpensemanager.helper.Utils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpensePieChartFrag extends Fragment {
    public static int showDialog = 0;
    String statusText = " Spin below PieCart";
    List<ExpenseIncomePojo> expenseIncomePojos;
    TextView exIncomeBoardTV;
    PieChart pieChart;
    TextView statusTV,monthNameTV;
    String select_month, select_year;
    List<String> monthName = new ArrayList<>();
    List<String> year = new ArrayList<>();
    ImageView fillterData;

    public ExpensePieChartFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = view.findViewById(R.id.piechart_1);
        exIncomeBoardTV = view.findViewById(R.id.exIncomeBoardTV);
        statusTV = view.findViewById(R.id.emptyTV);
        fillterData = view.findViewById(R.id.fillterData);
        monthNameTV = view.findViewById(R.id.monthNameTV);
        statusTV.setText(statusText);

        select_month = Utils.getMonthName();
        select_year = Utils.getYear();
        monthNameTV.setText(select_month+" "+select_year);
        fillterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthName = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDistinctMonthName();
                year = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDistanctYear();

                if (monthName.size() == 0) {
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
                        monthNameTV.setText(select_month+" "+select_year);
                        bottomSheetDialog.dismiss();

                    }
                });
                bottomSheetView.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveCSVfile();

                        bottomSheetDialog.dismiss();
                    }
                });


                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });

        featchData();


    }

    private void featchData() {
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getDescription().setText("Expense Data");
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("Expense");
        pieChart.setCenterTextSize(25f);
        pieChart.animateY(1000, Easing.EaseInBack);


        List<String> categoriesList = new ArrayList<>();
        expenseIncomePojos = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(select_month, select_year);

        if (expenseIncomePojos.size() == 0) {
            statusText = "Empty data insert income/expense";
            statusTV.setText(statusText);
            pieChart.setCenterText("Empty Pie Data");

        }

        for (ExpenseIncomePojo expenseIncomePojo : expenseIncomePojos) {
            String catagories = expenseIncomePojo.getE_catagories();
            categoriesList.add(catagories);
        }
        //For Remove Duplicate Categories
        Set<String> s = new LinkedHashSet<String>(categoriesList);
        categoriesList.clear();
        categoriesList.addAll(s);

        //For calculation Separate categories amount
        HashMap<String, Double> calcutateAmount = new HashMap<>();
        for (String data : categoriesList) {
            double value = 0.0;
            for (ExpenseIncomePojo expenseIncomeCalcution : expenseIncomePojos) {
                String cata = expenseIncomeCalcution.getE_catagories();
                if (data.equals(cata)) {
                    value = value + Double.parseDouble(expenseIncomeCalcution.getE_amount());
                }
            }
            calcutateAmount.put(data, value);
        }


        pieChart.animateY(1000, Easing.EaseInBack);
        List<PieEntry> yValues = new ArrayList<>();
        String catagoryWiseCalcultaion = "";
        for (Map.Entry specificData : calcutateAmount.entrySet()) {
            String name = specificData.getKey().toString().trim();
            String value = specificData.getValue().toString().trim();
            if (name.equals("Income")) {
            } else {
                yValues.add(new PieEntry(Float.valueOf(value), name));
                catagoryWiseCalcultaion = catagoryWiseCalcultaion + name + "   :   " + value + "\n";
            }

        }

        exIncomeBoardTV.setText(catagoryWiseCalcultaion);

        PieDataSet dataSet = new PieDataSet(yValues, null);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
    }

    private void saveCSVfile() {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "DailyBook");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "expenseIncome" + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            ExpenseIncomeDatabase db = ExpenseIncomeDatabase.getInstance(getContext());
           // Cursor curCSV = db.query("SELECT * FROM " + "ExpenseIncomeTbl", null);
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

}