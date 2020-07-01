package com.rztechtunes.dailyexpensemanager;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rztechtunes.dailyexpensemanager.adapter.ExpenseIncomeAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.helper.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExIncomeDashBoardFrag extends Fragment {

    TextView monthNameTV, totalIncomeTV, totalExpenesTV, totalBalanceTV;
    Spinner monthSP, yearSP;
    String select_month, select_year;
    RecyclerView expenseIncomeRV;
    Button searchBtn;
    double total_income = 0.0, total_expense = 0.0, total_balance = 0.0;

    public ExIncomeDashBoardFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ex_income_dash_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        monthNameTV = view.findViewById(R.id.monthNameTV);
        totalExpenesTV = view.findViewById(R.id.totalExpenseTV);
        totalBalanceTV = view.findViewById(R.id.totalBalanceTV);
        totalIncomeTV = view.findViewById(R.id.totalIncomeTV);
        monthSP = view.findViewById(R.id.selectMonthSP);
        yearSP = view.findViewById(R.id.selectYearSP);
        expenseIncomeRV = view.findViewById(R.id.expenseIncomeRV);
        searchBtn = view.findViewById(R.id.searchBtn);

        Button save = view.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File exportDir = new File(Environment.getExternalStorageDirectory(), "ExpenseIncomeManager");
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }

                File file = new File(exportDir, "expenseIncome" + ".csv");
                try {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    ExpenseIncomeDatabase db = ExpenseIncomeDatabase.getInstance(getContext());
                    Cursor curCSV = db.query("SELECT * FROM " + "ExpenseIncomeTbl", null);
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
                            .setContentText("Path: Internal Storage/ExpenseIncomeManager/expenseIncome.csv")
                            .show();


                } catch (Exception sqlEx) {
                    Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                }
            }
        });


        List<String> monthName = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDistinctMonthName();
        List<String> year = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDistanctYear();

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, monthName);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, year);
        monthSP.setAdapter(monthAdapter);
        yearSP.setAdapter(yearAdapter);


        monthSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_month = parent.getItemAtPosition(position).toString();
                monthNameTV.setText(select_month);
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

        //Call Method for featchDatavia month & year
        featchData();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                total_balance = 0.0;
                total_income = 0.0;
                total_expense = 0.0;
                featchData();
            }
        });


    }


    private void featchData() {

        List<ExpenseIncomePojo> expenseIncomePojoList = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(select_month, select_year);
        for (ExpenseIncomePojo expenseIncomePojo : expenseIncomePojoList) {
            String catagories = expenseIncomePojo.getE_catagories();
            if (catagories.equals("Income")) {
                total_income = total_income + Double.valueOf(expenseIncomePojo.getE_amount());

            } else {
                total_expense = total_expense + Double.valueOf(expenseIncomePojo.getE_amount());
            }

        }
        total_balance = total_income - total_expense;

        totalIncomeTV.setText(String.valueOf(total_income));
        totalExpenesTV.setText(String.valueOf(total_expense));
        totalBalanceTV.setText(String.valueOf(total_balance));

        ExpenseIncomeAdapter expenseIncomeAdapter = new ExpenseIncomeAdapter(getActivity(), expenseIncomePojoList);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        expenseIncomeRV.setLayoutManager(llm);
        expenseIncomeRV.setAdapter(expenseIncomeAdapter);
    }
}