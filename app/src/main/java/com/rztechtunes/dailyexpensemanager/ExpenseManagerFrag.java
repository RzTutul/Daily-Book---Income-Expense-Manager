package com.rztechtunes.dailyexpensemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.rztechtunes.dailyexpensemanager.adapter.ExpenseIncomeAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.helper.Utils;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

import java.util.List;

public class ExpenseManagerFrag extends Fragment {

    ExtendedFloatingActionButton addExpenseBtn;
    private String exCatagories;
    private String e_date;
    RecyclerView expenseRV;
    List<ExpenseIncomePojo> expenseIncomePojos;

    TextView incomeTV, expenseTV, balanceTV, monthTV;
    double t_income, t_expense, t_balance;


    public ExpenseManagerFrag() {
        // Required empty public constructor
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

        addExpenseBtn = view.findViewById(R.id.addexpenseBtn);
        expenseRV = view.findViewById(R.id.expenseRV);
        incomeTV = view.findViewById(R.id.incomeTV);
        expenseTV = view.findViewById(R.id.expenesTV);
        balanceTV = view.findViewById(R.id.balanceTV);
        monthTV = view.findViewById(R.id.monthTV);

        monthTV.setText(Utils.getMonthName());


        //FloatingBtn Click Listener
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showExpenseDilog();
            }
        });


        //Method for query all expense Data and show RV
        featchData();


    }

    private void featchData() {


        expenseIncomePojos = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(Utils.getMonthName(), Utils.getYear());
        for (ExpenseIncomePojo expenseIncomePojo : expenseIncomePojos) {
            String catagories = expenseIncomePojo.getE_catagories();
            if (catagories.equals("Income")) {
                t_income = t_income + Double.valueOf(expenseIncomePojo.getE_amount());
            } else {
                t_expense = t_expense + Double.valueOf(expenseIncomePojo.getE_amount());
            }

        }
        t_balance = t_income - t_expense;

        incomeTV.setText(String.valueOf(t_income));
        expenseTV.setText(String.valueOf(t_expense));
        balanceTV.setText(String.valueOf(t_balance));


        ExpenseIncomeAdapter expenseIncomeAdapter = new ExpenseIncomeAdapter(getActivity(), expenseIncomePojos);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        expenseRV.setLayoutManager(llm);
        expenseRV.setAdapter(expenseIncomeAdapter);

    }

    private void showExpenseDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(" Add Expense");
        builder.setIcon(R.drawable.ic_baseline_add_circle_outline_24);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_dialog, null);

        builder.setView(view1);
        final EditText expenseNameET = view1.findViewById(R.id.expenseNameET);
        final EditText expenseAmoutET = view1.findViewById(R.id.expenseAmountET);
        final Spinner expenseCatagoriesSP = view1.findViewById(R.id.expenseCatagories);

        final Button canelbtn = view1.findViewById(R.id.cancelBtn);
        final Button addexpenseBtn = view1.findViewById(R.id.addbtn);
        String[] catagories = {"Select Catagories", "Income", "Food&Drink", "Bank", "Shopping", "Transport", "Hotel", "Other"};


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, catagories);
        expenseCatagoriesSP.setAdapter(arrayAdapter);


        expenseCatagoriesSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exCatagories = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final AlertDialog dialog = builder.create();
        dialog.show();
        addexpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = expenseNameET.getText().toString();
                String amount = expenseAmoutET.getText().toString();

                if (ename.isEmpty()) {
                    expenseNameET.setError("Provide expense name!");
                } else if (amount.isEmpty()) {
                    expenseAmoutET.setError("Provide expense amount!");
                } else {
                    ExpenseIncomePojo expensePojo = new ExpenseIncomePojo(ename, exCatagories, amount, Utils.getDateWithTime(), Utils.getMonthName(), Utils.getYear());

                    long insert = ExpenseIncomeDatabase.getInstance(getActivity()).getExpenseIncomeDao().InsertValue(expensePojo);

                    if (insert > 0) {
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        featchData();
                    } else {
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();

                    }
                    dialog.dismiss();
                }

            }
        });
        canelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
}