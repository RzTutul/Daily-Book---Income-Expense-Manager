package com.rztechtunes.dailyexpensemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

import java.util.List;

@Dao
public interface ExpenseIncomeDao {

    @Insert
    long InsertValue(ExpenseIncomePojo dataPojo);

    @Update
    int updateValue(ExpenseIncomePojo dataPojo);
    @Delete
    int deleteExIncome(ExpenseIncomePojo expenseIncomePojo);

    @Query("Select * from expenseincometbl where e_id like:id")
    ExpenseIncomePojo getAllDatabyId(int id);

    @Query("Select * from expenseincometbl order by e_id desc")
    List<ExpenseIncomePojo> getAllData();

    @Query("Select DISTINCT e_month from expenseincometbl ")
    List<String> getDistinctMonthName();

    @Query("Select DISTINCT e_year from expenseincometbl ")
    List<String> getDistanctYear();


    @Query("Select * from expenseincometbl where e_month=:month and e_year=:year order by e_id desc")
    List<ExpenseIncomePojo> getDataByMonthYear(String month, String year);



}
