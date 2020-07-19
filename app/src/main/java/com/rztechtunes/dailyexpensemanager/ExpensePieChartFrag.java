package com.rztechtunes.dailyexpensemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.helper.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpensePieChartFrag extends Fragment {
    String statusText = " Spin below PieCart";
    List<ExpenseIncomePojo> expenseIncomePojos;
    TextView exIncomeBoardTV;
    PieChart pieChart;
    TextView statusTV;

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

        statusTV.setText(statusText);


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
        expenseIncomePojos = ExpenseIncomeDatabase.getInstance(getContext()).getExpenseIncomeDao().getDataByMonthYear(Utils.getMonthName(), Utils.getYear());

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
                catagoryWiseCalcultaion = catagoryWiseCalcultaion + name + ": " + value + "\n";
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
}