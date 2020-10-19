package com.rztechtunes.dailyexpensemanager.entites;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ExpenseIncomeTbl")
public class ExpenseIncomePojo {
    @PrimaryKey(autoGenerate = true)
    long e_id;
    String e_name;
    String e_catagories;
    String e_amount;
    String e_date;
    String e_month;
    String e_year;

    @Ignore
    public ExpenseIncomePojo(long e_id, String e_name, String e_catagories, String e_amount, String e_date, String e_month, String e_year) {
        this.e_id = e_id;
        this.e_name = e_name;
        this.e_catagories = e_catagories;
        this.e_amount = e_amount;
        this.e_date = e_date;
        this.e_month = e_month;
        this.e_year = e_year;
    }

    public ExpenseIncomePojo(String e_name, String e_catagories, String e_amount, String e_date, String e_month, String e_year) {
        this.e_name = e_name;
        this.e_catagories = e_catagories;
        this.e_amount = e_amount;
        this.e_date = e_date;
        this.e_month = e_month;
        this.e_year = e_year;
    }



    public String getE_month() {
        return e_month;
    }

    public void setE_month(String e_month) {
        this.e_month = e_month;
    }

    public String getE_year() {
        return e_year;
    }

    public void setE_year(String e_year) {
        this.e_year = e_year;
    }

    public long getE_id() {
        return e_id;
    }

    public void setE_id(long e_id) {
        this.e_id = e_id;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public String getE_catagories() {
        return e_catagories;
    }

    public void setE_catagories(String e_catagories) {
        this.e_catagories = e_catagories;
    }

    public String getE_amount() {
        return e_amount;
    }

    public void setE_amount(String e_amount) {
        this.e_amount = e_amount;
    }

    public String getE_date() {
        return e_date;
    }

    public void setE_date(String e_date) {
        this.e_date = e_date;
    }
}
