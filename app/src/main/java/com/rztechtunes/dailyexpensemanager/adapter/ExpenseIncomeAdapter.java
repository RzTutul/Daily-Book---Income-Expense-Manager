package com.rztechtunes.dailyexpensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rztechtunes.dailyexpensemanager.R;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

import java.util.List;

public class ExpenseIncomeAdapter extends RecyclerView.Adapter<ExpenseIncomeAdapter.ExpenseViewHolder> {
    private Context context;
    private List<ExpenseIncomePojo> expensePojos;


    public ExpenseIncomeAdapter(Context context, List<ExpenseIncomePojo> expensePojos) {
        this.context = context;
        this.expensePojos = expensePojos;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.expense_row, parent, false);


        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        final ExpenseIncomePojo expensePojo = expensePojos.get(position);

        String eCata = expensePojo.getE_catagories();

        if (eCata.equals("Income")) {
            holder.expenseName.setText(expensePojos.get(position).getE_name());
            holder.expenseCatagories.setText(expensePojos.get(position).getE_catagories());
            holder.expenseAmount.setText("+৳ " + String.valueOf(expensePojos.get(position).getE_amount()));
           holder.expenseDate.setText(expensePojos.get(position).getE_date());
            holder.expenseCatagories.setTextColor(Color.RED);
        } else {
            holder.expenseName.setText(expensePojos.get(position).getE_name());
            holder.expenseCatagories.setText(expensePojos.get(position).getE_catagories());
           holder.expenseAmount.setText("-৳ " + String.valueOf(expensePojos.get(position).getE_amount()));
            holder.expenseDate.setText(expensePojos.get(position).getE_date());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return expensePojos.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseAmount, expenseDate, expenseCatagories;
        ImageView editbtn, deletebtn;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseName = itemView.findViewById(R.id.row_expenseName);
            expenseAmount = itemView.findViewById(R.id.row_expenseAmount);
            expenseCatagories = itemView.findViewById(R.id.row_expense_catagories);
            expenseDate = itemView.findViewById(R.id.row_expense_date);
            editbtn = itemView.findViewById(R.id.row_expenseEdit);
            deletebtn = itemView.findViewById(R.id.row_expenseDelete);
        }


    }


}
