package com.rztechtunes.dailyexpensemanager.pojo;

public class CatagoriesSPPojo {
    String cataName;
    int cataImage;

    public CatagoriesSPPojo(String cataName, int cataImage) {
        this.cataName = cataName;
        this.cataImage = cataImage;
    }

    public String getCataName() {
        return cataName;
    }

    public void setCataName(String cataName) {
        this.cataName = cataName;
    }

    public int getCataImage() {
        return cataImage;
    }

    public void setCataImage(int cataImage) {
        this.cataImage = cataImage;
    }
}
