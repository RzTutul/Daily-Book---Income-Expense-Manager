package com.rztechtunes.dailyexpensemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.rztechtunes.dailyexpensemanager.R;
import com.rztechtunes.dailyexpensemanager.db.ExpenseIncomeDatabase;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.airbnb.lottie.L.TAG;

public class DairyRVAdapter extends RecyclerView.Adapter<DairyRVAdapter.DairyViewHolder> {

    Context context;
    List<DairyPojo> dairyPojoList;


    public DairyRVAdapter(Context context, List<DairyPojo> dairyPojoList) {
        this.context = context;
        this.dairyPojoList = dairyPojoList;

    }

    @NonNull
    @Override
    public DairyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflatere = LayoutInflater.from(context);

        View view = inflatere.inflate(R.layout.dairy_row_layout, parent, false);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.layout_animation);
        view.startAnimation(animation);
        return new DairyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DairyViewHolder holder, final int position) {
        String date = dairyPojoList.get(position).getDate();
        String dateSplit[] = date.split("\\.", 4);


        holder.titleTV.setText(dairyPojoList.get(position).getTitle());
        holder.noteTV.setText(dairyPojoList.get(position).getNote());
        holder.dayTV.setText(dateSplit[0]);
        holder.dateTV.setText(dateSplit[1]);
        //holder.monthYearTV.setText("In "+dateSplit[2]+" "+dateSplit[3]);


        DairyPojo dairyPojo = dairyPojoList.get(position);
        if (("Happy").equals(dairyPojo.getMood())) {
            holder.moodImageView.setImageResource(R.drawable.ic_happy);
        } else if (("Sad").equals(dairyPojo.getMood())) {
            holder.moodImageView.setImageResource(R.drawable.ic_unhappy);
        } else if (("Love").equals(dairyPojo.getMood())) {
            holder.moodImageView.setImageResource(R.drawable.ic_in_love);
        } else if (("Sick").equals(dairyPojo.getMood())) {
            holder.moodImageView.setImageResource(R.drawable.ic_ill);
        } else if (("Surprised").equals(dairyPojo.getMood())) {
            holder.moodImageView.setImageResource(R.drawable.ic_surprised);
        } else if (("Angry").equals(dairyPojo.getMood())) {
            holder.moodImageView.setImageResource(R.drawable.ic_mad);
        } else if (("Funny").equals(dairyPojo.getMood())) {
            holder.moodImageView.setImageResource(R.drawable.ic_tongue_out);
        }

    /*    holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this file!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                DairyPojo dairyPojo = dairyPojoList.get(position);
                                ExpenseIncomeDatabase.getInstance(context).getDairyDao().deleteDairy(dairyPojo);
                                Navigation.findNavController(holder.itemView).navigate(R.id.dailyNoteFragment);
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
        return dairyPojoList.size();
    }

    public void removeFromDatabase(DairyPojo dairyPojo) {

        dairyPojoList.remove(dairyPojo);
        //  dairyViewModel.deleteDairy(dairyPojo.getEventID(),dairyPojo.getDairyID());
    }

    public class DairyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV, noteTV, dateTV, dayTV, monthYearTV;
        TextView sharebtn;
        ImageView moodImageView;

        public DairyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTV);
            noteTV = itemView.findViewById(R.id.noteTV);
            dayTV = itemView.findViewById(R.id.dayNameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            moodImageView = itemView.findViewById(R.id.moodImageView);

        }
    }
}
