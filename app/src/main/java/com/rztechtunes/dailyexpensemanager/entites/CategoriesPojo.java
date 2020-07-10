package com.rztechtunes.dailyexpensemanager.entites;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cataTbl")
public class CategoriesPojo {
    @PrimaryKey(autoGenerate = true)
    long cata_id;
    String cata_name;

    @Ignore
    public CategoriesPojo(long cata_id, String cata_name) {
        this.cata_id = cata_id;
        this.cata_name = cata_name;
    }

    public CategoriesPojo(String cata_name) {
        this.cata_name = cata_name;
    }

    public long getCata_id() {
        return cata_id;
    }

    public String getCata_name() {
        return cata_name;
    }
}
