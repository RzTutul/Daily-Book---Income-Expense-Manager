package com.rztechtunes.dailyexpensemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.rztechtunes.dailyexpensemanager.adapter.ExpenseIncomeAdapter;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;
import com.rztechtunes.dailyexpensemanager.helper.Utils;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

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
    LinearLayout pieCharBtnLL;
    CardView emptyCV;
    CollapsingToolbarLayout title;
    ExtendedFloatingActionButton addExpenseBtn;
    private String exCatagories;
    private String e_date;
    RecyclerView expenseRV;
    List<ExpenseIncomePojo> expenseIncomePojos = new ArrayList<>();
    List<String> categoriesPojoList = new ArrayList<>();

    TickerView incomeTV, expenseTV, balanceTV;
    ExpenseIncomeAdapter expenseIncomeAdapter;

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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        addExpenseBtn = view.findViewById(R.id.addexpenseBtn);
        expenseRV = view.findViewById(R.id.expenseRV);
        incomeTV = view.findViewById(R.id.incomeTV);
        expenseTV = view.findViewById(R.id.expenesTV);
        balanceTV = view.findViewById(R.id.balanceTV);
        title = view.findViewById(R.id.monthTitle);
        emptyCV = view.findViewById(R.id.emptyCardView);
        pieCharBtnLL = view.findViewById(R.id.pieCharBtnLL);


        //For TextViewCountAnimation
        incomeTV.setCharacterLists(TickerUtils.provideNumberList());
        expenseTV.setCharacterLists(TickerUtils.provideNumberList());
        balanceTV.setCharacterLists(TickerUtils.provideNumberList());


        title.setTitle(Utils.getMonthName());

        //FloatingBtn Click Listener
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showExpenseDilog();
            }
        });

        emptyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseDilog();
            }
        });

        pieCharBtnLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPieChartDialog();
            }
        });


        ///featch Categories Data if it's empty then insert.
        categoriesPojoList = ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().getAllCatagories();

        if (categoriesPojoList.isEmpty()) {
            final String[] catagories = {"Select Categories", "Income", "Food&Drink", "Medicine", "Shopping", "Cell phone", "Rent", "Transport", "Hotel", "Other"};

            for (int i = 0; i < catagories.length; i++) {
                CategoriesPojo categoriesPojo = new CategoriesPojo(catagories[i]);
                ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().InsertCatagoires(categoriesPojo);
            }

        }

        //Method for query all expense Data and show RV
        featchData();


    }

    private void featchData() {
        double t_income = 0.0, t_expense = 0.0, t_balance = 0.0;

        expenseIncomePojos = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(Utils.getMonthName(), Utils.getYear());

        if (expenseIncomePojos.size() == 0) {
            emptyCV.setVisibility(View.VISIBLE);

            expenseIncomeAdapter = new ExpenseIncomeAdapter(getActivity(), expenseIncomePojos);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            expenseRV.setLayoutManager(llm);
            expenseRV.setAdapter(expenseIncomeAdapter);

            incomeTV.setText(decimalFormat.format(t_income));
            expenseTV.setText(decimalFormat.format(t_expense));
            balanceTV.setText(decimalFormat.format(t_balance));


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

            incomeTV.setText(decimalFormat.format(t_income));
            expenseTV.setText(decimalFormat.format(t_expense));
            balanceTV.setText(decimalFormat.format(t_balance));


            expenseIncomeAdapter = new ExpenseIncomeAdapter(getActivity(), expenseIncomePojos);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            expenseRV.setLayoutManager(llm);
            expenseRV.setAdapter(expenseIncomeAdapter);

            //Scrolling RV Floating button hide/Show
            expenseRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            addExpenseBtn.show();
                            break;
                        default:
                            addExpenseBtn.hide();
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


    private void showExpenseDilog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(" Add Income/Expense");
        builder.setIcon(R.drawable.ic_baseline_add_circle_outline_24);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.add_expense_dialog, null);

        builder.setView(view1);
        final EditText expenseNameET = view1.findViewById(R.id.expenseNameET);
        final EditText expenseAmoutET = view1.findViewById(R.id.expenseAmountET);
        final Spinner expenseCatagoriesSP = view1.findViewById(R.id.expenseCatagories);

        final Button canelbtn = view1.findViewById(R.id.cancelBtn);
        final Button addexpenseBtn = view1.findViewById(R.id.addbtn);
        List<String> catagories = ExpenseIncomeDatabase.getInstance(getContext()).getCataDao().getAllCatagories();

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
                    expenseNameET.setError("Provide name!");
                } else if (amount.isEmpty()) {
                    expenseAmoutET.setError("Provide amount!");
                }
                else if (exCatagories.equals("Select Categories")) {
                    TextView errorText = (TextView) expenseCatagoriesSP.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select Categories");

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_piechart:
                showPieChartDialog();
                break;

        }
        return super.onOptionsItemSelected(item);


    }

    private void showPieChartDialog() {
        String statusText = " Spin below PieCart";
        List<String> categoriesList = new ArrayList<>();
        expenseIncomePojos = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(Utils.getMonthName(), Utils.getYear());

        if (expenseIncomePojos.size() == 0) {
            statusText = "Empty data insert income/expense";
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


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Expense-Income");
        builder.setIcon(R.drawable.icons8_combo_chart_48px);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.pie_chart_dialog, null);

        builder.setView(view1);

        PieChart pieChart = view1.findViewById(R.id.piechart_1);
        TextView exIncomeBoardTV = view1.findViewById(R.id.exIncomeBoardTV);
        TextView statusTV = view1.findViewById(R.id.statusTV);

        statusTV.setText(statusText);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.animateY(1000, Easing.EaseInBack);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        String catagoryWiseCalcultaion = "";
        for (Map.Entry specificData : calcutateAmount.entrySet()) {
            String name = specificData.getKey().toString().trim();
            String value = specificData.getValue().toString().trim();

            yValues.add(new PieEntry(Float.valueOf(value), name));
            catagoryWiseCalcultaion = catagoryWiseCalcultaion + name + ": " + value + "\n";

        }

        exIncomeBoardTV.setText(catagoryWiseCalcultaion);

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);

        AlertDialog dialog = builder.create();

        dialog.show();


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_option, menu);
    }
}