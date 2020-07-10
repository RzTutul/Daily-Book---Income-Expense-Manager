package com.rztechtunes.dailyexpensemanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.rztechtunes.dailyexpensemanager.dao.CatagoriesDao;
import com.rztechtunes.dailyexpensemanager.dao.DairyDao;
import com.rztechtunes.dailyexpensemanager.dao.ExpenseIncomeDao;
import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

@Database(entities = {ExpenseIncomePojo.class, DairyPojo.class, CategoriesPojo.class},version = 1,exportSchema = false)
public abstract class ExpenseIncomeDatabase extends RoomDatabase {

    public abstract ExpenseIncomeDao getExpenseIncomeDao();
    public abstract DairyDao getDairyDao();
    public abstract CatagoriesDao getCataDao();
    public static ExpenseIncomeDatabase db;

    public static ExpenseIncomeDatabase getInstance(Context context) //Create SingleTone object
    {
        if (db == null) {
            db = Room.databaseBuilder(context, ExpenseIncomeDatabase.class, "ExIn_DB").allowMainThreadQueries().build();
            return db;
        }
        return db;
    }



}
