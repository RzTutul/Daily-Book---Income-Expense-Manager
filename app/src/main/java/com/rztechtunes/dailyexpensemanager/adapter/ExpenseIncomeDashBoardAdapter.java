package com.rztechtunes.dailyexpensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.rztechtunes.dailyexpensemanager.R;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

import java.util.List;


public class ExpenseIncomeDashBoardAdapter extends RecyclerView.Adapter<ExpenseIncomeDashBoardAdapter.ExpenseViewHolder> {
    private Context context;
    private List<ExpenseIncomePojo> expensePojos;


    public ExpenseIncomeDashBoardAdapter(Context context, List<ExpenseIncomePojo> expensePojos) {
        this.context = context;
        this.expensePojos = expensePojos;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.expense_row, parent, false);

        Animation animation = AnimationUtils.loadAnimation(context,R.anim.layout_animation);
        view.startAnimation(animation);

        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpenseViewHolder holder, final int position) {
        final ExpenseIncomePojo expensePojo = expensePojos.get(position);

        String eCata = expensePojo.getE_catagories();
        String day[] = (expensePojo.getE_date()).split("-");
        String month = expensePojo.getE_month();
        String dayName[] = (expensePojos.get(position).getE_date()).split("-");


        if (eCata.equals("Income")) {

            holder.expenseCatagories.setTextColor(Color.RED);
            holder.expenseAmount.setText("+৳ " + String.valueOf(expensePojos.get(position).getE_amount()));

        } else {
            holder.expenseAmount.setText("-৳ " + String.valueOf(expensePojos.get(position).getE_amount()));
        }
        holder.expenseName.setText(expensePojos.get(position).getE_name());
        holder.expenseCatagories.setText(expensePojos.get(position).getE_catagories());
        holder.expenseDate.setText(expensePojos.get(position).getE_date()+" "+expensePojos.get(position).getE_month()+" "+expensePojos.get(position).getE_year());




    }


    @Override
    public int getItemCount() {
        return expensePojos.size();
    }


    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseAmount, expenseDate, expenseCatagories, dayTV, monthNameTV;


        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseName = itemView.findViewById(R.id.row_expenseName);
            expenseAmount = itemView.findViewById(R.id.row_expenseAmount);
            expenseCatagories = itemView.findViewById(R.id.row_expense_catagories);
            expenseDate = itemView.findViewById(R.id.row_expense_date);


        }


    }


}
