package com.rztechtunes.dailyexpensemanager.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rztechtunes.dailyexpensemanager.dao.CatagoriesDao;
import com.rztechtunes.dailyexpensemanager.dao.DairyDao;
import com.rztechtunes.dailyexpensemanager.dao.ExpenseIncomeDao;
import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;
import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

@Database(entities = {ExpenseIncomePojo.class, DairyPojo.class, CategoriesPojo.class},version = 2,exportSchema = false)
public abstract class ExpenseIncomeDatabase extends RoomDatabase {

    public abstract ExpenseIncomeDao getExpenseIncomeDao();
    public abstract DairyDao getDairyDao();
    public abstract CatagoriesDao getCataDao();
    public static ExpenseIncomeDatabase db;


    public static Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("alter table 'dairyTbl' add column 'mood' text");
        }
    };

    public static ExpenseIncomeDatabase getInstance(Context context) //Create SingleTone object
    {
        if (db == null) {
            db = Room.databaseBuilder(context, ExpenseIncomeDatabase.class, "ExIn_DB")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();
            return db;
        }
        return db;
    }

}
