package com.rztechtunes.dailyexpensemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rztechtunes.dailyexpensemanager.entites.DairyPojo;
import com.rztechtunes.dailyexpensemanager.entites.ExpenseIncomePojo;

import java.util.List;

@Dao
public interface DairyDao {

    @Insert
    long inserDairy(DairyPojo dairyPojo);

    @Update
    int updateDairy(DairyPojo dairyPojo);

    @Delete
    int deleteDairy(DairyPojo dairyPojo);

    @Query("Select * from dairyTbl order by dairyid desc ")
    List<DairyPojo> getAllDairyList();

    @Query("Select * from dairyTbl where dairyid like:id ")
    DairyPojo getDairyByID(long id);

}
