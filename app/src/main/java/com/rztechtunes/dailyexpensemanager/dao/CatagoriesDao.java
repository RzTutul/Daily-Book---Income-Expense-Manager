package com.rztechtunes.dailyexpensemanager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.rztechtunes.dailyexpensemanager.entites.CategoriesPojo;

import java.util.List;

@Dao
public interface CatagoriesDao {

    @Insert
    long InsertCatagoires (CategoriesPojo categoriesPojo);

    @Query("Select cata_name from cataTbl")
    List<String> getAllCatagories();
}
