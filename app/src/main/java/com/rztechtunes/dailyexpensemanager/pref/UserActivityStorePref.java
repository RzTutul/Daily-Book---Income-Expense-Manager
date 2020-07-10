package com.rztechtunes.dailyexpensemanager.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class UserActivityStorePref {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserActivityStorePref(Context context) {
        sharedPreferences = context.getSharedPreferences("UserActivity",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setPatterKey(String patterKey)
    {
        editor.putString("key",patterKey);
        editor.commit();
    }

    public String getPatternKey()
    {
      return sharedPreferences.getString("key","543543210");
    }

    public void setPersonName(String name) {
        editor.putString("name", name);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString("name", "tutulxcvd");
    }

    public void setPatterActivity(boolean isLunch) {
        editor.putBoolean("isLunch", isLunch);
        editor.commit();
    }

    public boolean getPatternActivity() {
        return sharedPreferences.getBoolean("isLunch", false);
    }

    public void setPatternSwithStatus(boolean status) {
        editor.putBoolean("status", status);
        editor.commit();
    }

    public boolean getPatterSwithStatus() {
        return sharedPreferences.getBoolean("status", false);
    }

    public void setNotfication(boolean switchstatus) {
        editor.putBoolean("notificaiton", switchstatus);
        editor.commit();
    }

    public boolean getNotification() {
        return sharedPreferences.getBoolean("notificaiton",false);
    }

}
