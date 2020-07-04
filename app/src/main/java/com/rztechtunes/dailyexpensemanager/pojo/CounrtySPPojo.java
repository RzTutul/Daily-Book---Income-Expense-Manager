package com.rztechtunes.dailyexpensemanager.pojo;

public class CounrtySPPojo {
    String countyName;
    String dolarName;
    int countryImages;


    public CounrtySPPojo(String countyName, String dolarName, int countryImages) {
        this.countyName = countyName;
        this.dolarName = dolarName;
        this.countryImages = countryImages;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getDolarName() {
        return dolarName;
    }

    public int getCountryImages() {
        return countryImages;
    }
}
