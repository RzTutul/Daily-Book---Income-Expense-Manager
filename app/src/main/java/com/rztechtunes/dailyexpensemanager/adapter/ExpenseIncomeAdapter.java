package com.rztechtunes.dailyexpensemanager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.rztechtunes.dailyexpensemanager.AddTransactionFrag;
import com.rztechtunes.dailyexpensemanager.R;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;
import com.rztechtunes.dailyexpensemanager.helper.Utils;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ExpenseIncomeAdapter extends RecyclerView.Adapter<ExpenseIncomeAdapter.ExpenseViewHolder> {
    private Context context;
    private List<ExpenseIncomePojo> expensePojos;
    private String exCatagories, Catagories;


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
    public void onBindViewHolder(@NonNull final ExpenseViewHolder holder, final int position) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.layout_animation);
        holder.itemView.startAnimation(shake);

        final ExpenseIncomePojo expensePojo = expensePojos.get(position);
        String eCata = expensePojo.getE_catagories();
        String day[] = (expensePojo.getE_date()).split("-");
        String month = expensePojo.getE_month();
        String dayName[] = (expensePojos.get(position).getE_date()).split("-");


        if (eCata.equals("Income")) {
       
            holder.expenseCatagories.setTextColor(Color.parseColor("#8CEF35"));
            holder.expenseAmount.setTextColor(Color.parseColor("#8CEF35"));
            holder.row_lineColor.setBackgroundColor(Color.GREEN);
            holder.iconImageView.setBackgroundResource(R.drawable.ic_income);
            holder.expenseAmount.setText("+ " + expensePojos.get(position).getE_amount() + " /=");
        } else if (eCata.equals("Food&Drink")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_drinks);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#DA4836"));
        }  else if (eCata.equals("Medicine")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_medicine);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#31cad0"));

        } else if (eCata.equals("Shopping")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_shopping);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#FBAD4A"));
        } else if (eCata.equals("Cell phone")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_mobile);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#EA348B"));
        } else if (eCata.equals("Rent")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_rent);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#d38a5a"));
        } else if (eCata.equals("Transport")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_transport);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#FFF7B733"));
        } else if (eCata.equals("Hotel")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_hotel);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#79d8eb"));
        } else if (eCata.equals("Other")) {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_expense);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#72c1e8"));
        }
        else
        {
            holder.expenseAmount.setText("- " + expensePojos.get(position).getE_amount() + " /=");
            holder.iconImageView.setBackgroundResource(R.drawable.ic_expense);
            holder.row_lineColor.setBackgroundColor(Color.parseColor("#72c1e8"));
        }


        holder.expenseName.setText(expensePojos.get(position).getE_name());
        holder.expenseCatagories.setText(expensePojos.get(position).getE_catagories());
        holder.expenseDate.setText(expensePojos.get(position).getE_date()+" "+expensePojos.get(position).getE_month()+" "+expensePojos.get(position).getE_year());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTransactionFrag.positionupdate = expensePojos.get(position).getE_id();
                Navigation.findNavController(holder.itemView).navigate(R.id.addTransactionFrag);

               // showUpdateAllertDialog(holder, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


                                ExpenseIncomePojo expenseIncomePojo = expensePojos.get(position);
                                ExpenseIncomeDatabase.getInstance(context).getExpenseIncomeDao().deleteExIncome(expenseIncomePojo);
                                Navigation.findNavController(holder.itemView).navigate(R.id.expenseManagerFrag);
                                sDialog.dismissWithAnimation();
                                //   Navigation.findNavController(holder.itemView).navigate(R.id.expenseManagerFrag);

                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return expensePojos.size();
    }


    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseAmount, expenseDate, expenseCatagories, dayTV, monthNameTV;
        ImageView iconImageView;
        View row_lineColor;


        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseName = itemView.findViewById(R.id.row_expenseName);
            expenseAmount = itemView.findViewById(R.id.row_expenseAmount);
            expenseCatagories = itemView.findViewById(R.id.row_expense_catagories);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            expenseDate = itemView.findViewById(R.id.row_expense_date);
            row_lineColor = itemView.findViewById(R.id.row_lineColor);


        }


    }

    private void showUpdateAllertDialog(final ExpenseViewHolder holder, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Data");
        builder.setIcon(R.drawable.icons8_checkmark_30px);
        final View view1 = LayoutInflater.from(context).inflate(R.layout.add_expense_dialog, null);

        builder.setView(view1);
        final EditText expenseNameET = view1.findViewById(R.id.expenseNameET);
        final EditText expenseAmoutET = view1.findViewById(R.id.expenseAmountET);
        final Spinner expenseCatagoriesSp = view1.findViewById(R.id.expenseCatagories);


        Button updatebtn = view1.findViewById(R.id.addbtn);
        Button cancelbtn = view1.findViewById(R.id.cancelBtn);

        exCatagories = expensePojos.get(position).getE_catagories();

        List<String> catagories = ExpenseIncomeDatabase.getInstance(context).getCataDao().getAllCatagories();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, catagories);
        expenseCatagoriesSp.setAdapter(arrayAdapter);
        int spinnerPosition = arrayAdapter.getPosition(exCatagories);
        expenseCatagoriesSp.setSelection(spinnerPosition);

        updatebtn.setText("Update");
        expenseNameET.setText(expensePojos.get(position).getE_name());
        expenseAmoutET.setText(String.valueOf(expensePojos.get(position).getE_amount()));

        expenseCatagoriesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Catagories = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final AlertDialog dialog = builder.create();

        dialog.show();

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = expenseNameET.getText().toString();
                String amount = expenseAmoutET.getText().toString();

                if (ename.equals("")) {
                    expenseNameET.setError("Empty Name");
                } else if (amount.equals("")) {
                    expenseAmoutET.setError("Empty Amount");
                } else if (Catagories.equals("Select Categories")) {
                    TextView errorText = (TextView) expenseCatagoriesSp.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select Valid Categories");
                } else {

                    ExpenseIncomePojo expenseIncomePojo = new ExpenseIncomePojo(expensePojos.get(position).getE_id(), ename, Catagories, amount, Utils.getDateWithTime(), Utils.getMonthName(), Utils.getYear());
                    int update = ExpenseIncomeDatabase.getInstance(context).getExpenseIncomeDao().updateValue(expenseIncomePojo);

                    if (update > 0) {
                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(holder.itemView).navigate(R.id.expenseManagerFrag);


                    } else {
                        Toast.makeText(context, "Updated fail", Toast.LENGTH_SHORT).show();

                    }
                    dialog.dismiss();
                }

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


}
