package com.rztechtunes.dailyexpensemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rztechtunes.dailyexpensemanager.adapter.CatagoriesSpinnerAdapter;
import com.rztechtunes.dailyexpensemanager.adapter.ExpenseIncomeAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.helper.Utils;
import com.rztechtunes.dailyexpensemanager.pojo.CatagoriesSPPojo;
import com.rztechtunes.dailyexpensemanager.pref.UserActivityStorePref;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class AddTransactionFrag extends Fragment {

    public static long positionupdate=0;
    Toolbar toolbar;
    Spinner catagoriesSp;
    String selectedCata;
    Button saveBtn, updateBtn;
    int[] catagoriesImage = {R.drawable.ic_baseline_arrow_drop_down_24, R.drawable.ic_income, R.drawable.ic_drinks, R.drawable.ic_medicine, R.drawable.ic_shopping, R.drawable.ic_mobile, R.drawable.ic_rent, R.drawable.ic_transport, R.drawable.ic_hotel, R.drawable.ic_expense};
    final String[] catagoriesName = {"Select Categories", "Income", "Food&Drink", "Medicine", "Shopping", "Cell phone", "Rent", "Transport", "Hotel", "Other"};
    List<CatagoriesSPPojo> catagoriesSPPojoList;
    List<String> catagoriesList;
    EditText expenesTittleET, expenseAmountET;
    DatePicker datePicker;
    TextView amountTV;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public AddTransactionFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        saveBtn = view.findViewById(R.id.saveBtn);
        catagoriesSp = view.findViewById(R.id.catagoriesSP);
        expenesTittleET = view.findViewById(R.id.expenesTittleET);
        expenseAmountET = view.findViewById(R.id.amountET);
        updateBtn = view.findViewById(R.id.updateBtn);
        datePicker = view.findViewById(R.id.datePicker);
        amountTV = view.findViewById(R.id.amountTV);
        catagoriesSPPojoList = new ArrayList<>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.expenseManagerFrag);
            }
        });

        UserActivityStorePref userActivityStorePref = new UserActivityStorePref(getContext());

        amountTV.setText(String.format("%s%s", userActivityStorePref.getCurrency(), getCurrentBalance()));

        ///featch Categories Data if it's empty then insert.
        catagoriesList = ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().getAllCatagories();
        if (catagoriesList.isEmpty()) {
            for (int i = 0; i < catagoriesName.length; i++) {
                CategoriesPojo categoriesPojo = new CategoriesPojo(catagoriesName[i]);
                ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().InsertCatagoires(categoriesPojo);
            }

        }


        /// Add Categories with images
        List<String> catagories = ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().getAllCatagories();
        for (int i = 0; i < 10; i++) {
            catagoriesSPPojoList.add(new CatagoriesSPPojo(catagoriesName[i], catagoriesImage[i]));
        }
        if (catagories.size() > 10) {

            for (int j = 10; j < catagories.size(); j++) {

                catagoriesSPPojoList.add(new CatagoriesSPPojo(catagories.get(j), catagoriesImage[9]));
            }
        }




        //For Update the Expense
        if (positionupdate >0) {
            updateBtn.setVisibility(View.VISIBLE);
            saveBtn.setVisibility(View.GONE);
            ExpenseIncomePojo expenseIncomePojo = ExpenseIncomeDatabase.getInstance(getActivity()).getExpenseIncomeDao().getAllDatabyId(positionupdate);
            toolbar.setTitle("Update Transaction");
            expenesTittleET.setText(expenseIncomePojo.getE_name());
            expenseAmountET.setText(expenseIncomePojo.getE_amount());

            int monthNumber =0;
            for (String month : MONTHS)
            {
                if (month.equals(expenseIncomePojo.getE_month()))
                {
                    break;
                }
                monthNumber++;
            }

            datePicker.init(Integer.parseInt(expenseIncomePojo.getE_year()),monthNumber,Integer.parseInt(expenseIncomePojo.getE_date()),null);

            String exCatagories = expenseIncomePojo.getE_catagories();
            CatagoriesSpinnerAdapter adapter = new CatagoriesSpinnerAdapter(getContext(), catagoriesSPPojoList);
            catagoriesSp.setAdapter(adapter);
            int spinnerPosition =1;
            //Find the position the categories from adapter
            for (CatagoriesSPPojo catagoriesSPPojo: catagoriesSPPojoList)
            {
                if ((catagoriesSPPojo.getCataName()).equals(exCatagories))
                {
                    break;
                }
                spinnerPosition++;
            }
            catagoriesSp.setSelection(spinnerPosition-1);
            catagoriesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCata = catagoriesSPPojoList.get(position).getCataName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }
        else
        {
            updateBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.VISIBLE);

            CatagoriesSpinnerAdapter adapter = new CatagoriesSpinnerAdapter(getContext(), catagoriesSPPojoList);
            catagoriesSp.setAdapter(adapter);
            catagoriesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCata = catagoriesSPPojoList.get(position).getCataName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }



        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = expenesTittleET.getText().toString();
                String amount = expenseAmountET.getText().toString();

                if (ename.equals("")) {
                    expenesTittleET.setError("Empty Name");
                } else if (amount.equals("")) {
                    expenseAmountET.setError("Empty Amount");
                } else if (selectedCata.equals("Select Categories")) {
                    TextView errorText = (TextView) catagoriesSp.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select Valid Categories");
                } else {

                    ExpenseIncomePojo expensePojo = new ExpenseIncomePojo(positionupdate,ename, selectedCata, amount,String.valueOf(datePicker.getDayOfMonth()), MONTHS[datePicker.getMonth()],String.valueOf(datePicker.getYear()));
                    int update = ExpenseIncomeDatabase.getInstance(getActivity()).getExpenseIncomeDao().updateValue(expensePojo);

                    if (update > 0) {
                        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigate(R.id.expenseManagerFrag);

                    } else {
                        Toast.makeText(getActivity(), "Updated fail", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });






        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String ename = expenesTittleET.getText().toString();
                final String amount = expenseAmountET.getText().toString();

                if (ename.isEmpty()) {
                    expenesTittleET.setError("Provide tittle!");
                } else if (amount.isEmpty()) {
                    expenseAmountET.setError("Provide amount!");
                } else if (selectedCata.equals("Select Categories")) {
                    Toast.makeText(getActivity(), "Select Categories !", Toast.LENGTH_SHORT).show();
                } else {

                    int monthNumber =Integer.parseInt(Utils.getMonthNumber());
                    Log.i(TAG, "monthnumber: "+datePicker.getMonth()+" "+monthNumber);
                    if (((datePicker.getMonth())+1)==monthNumber)
                    {
                        insertData(v, ename, amount);

                    }
                    else
                    {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you Sure?")
                                .setContentText("This data will add in "+MONTHS[datePicker.getMonth()] +" month")
                                .setConfirmText("Yes, Added!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        insertData(v, ename, amount);
                                        sDialog
                                                .setTitleText("Added!")
                                                .setContentText("Added has been Successful!")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    }
                                })
                                .show();



                    }


                }
            }
        });
    }

    private void insertData(View v, String ename, String amount) {
        ExpenseIncomePojo expensePojo = new ExpenseIncomePojo(ename, selectedCata, amount,String.valueOf(datePicker.getDayOfMonth()), MONTHS[datePicker.getMonth()],String.valueOf(datePicker.getYear()) );
        Log.i(TAG, "featch: "+String.valueOf(datePicker.getDayOfMonth())+ MONTHS[datePicker.getMonth()]+String.valueOf(datePicker.getYear()));
        long insert = ExpenseIncomeDatabase.getInstance(getActivity()).getExpenseIncomeDao().InsertValue(expensePojo);

        if (insert > 0) {
            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigate(R.id.expenseManagerFrag);
        } else {
            Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();

        }
    }


    private double getCurrentBalance()
    {
        double t_income = 0.0, t_expense = 0.0, t_balance = 0.0;

        Log.i(TAG, "featchData: "+Utils.getMonthName()+" "+Utils.getYear());
        List<ExpenseIncomePojo> expenseIncomePojos = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(Utils.getMonthName(), Utils.getYear());

        for (ExpenseIncomePojo expenseIncomePojo : expenseIncomePojos) {
            String catagories = expenseIncomePojo.getE_catagories();
            if (catagories.equals("Income")) {
                t_income = t_income + Double.valueOf(expenseIncomePojo.getE_amount());
            } else {
                t_expense = t_expense + Double.valueOf(expenseIncomePojo.getE_amount());
            }

        }
        t_balance = t_income - t_expense;


        return t_balance;
    }
}