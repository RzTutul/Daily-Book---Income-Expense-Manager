package com.rztechtunes.dailyexpensemanager.entites;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "dairyTbl")
public class DairyPojo {
    @PrimaryKey(autoGenerate = true)
    long dairyid;
    String date;
    String title;
    String note;
    String mood;


    @Ignore
    public DairyPojo(long dairyid, String date, String title, String note,String mood) {
        this.dairyid = dairyid;
        this.date = date;
        this.title = title;
        this.note = note;
        this.mood = mood;
    }

    public DairyPojo(String date, String title, String note,String mood) {
        this.date = date;
        this.title = title;
        this.note = note;
        this.mood = mood;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public long getDairyid() {
        return dairyid;
    }

    public void setDairyid(long dairyid) {
        this.dairyid = dairyid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
