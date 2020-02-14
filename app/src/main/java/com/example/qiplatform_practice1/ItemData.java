package com.example.qiplatform_practice1;

public class ItemData {
    private String strTitle;
    private String strDate;
    private String strNumber;

    public ItemData(String strTitle, String strDate, String strNumber) {
        this.strTitle = strTitle;
        this.strDate = strDate;
        this.strNumber = strNumber;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrNumber() {
        return strNumber;
    }

    public void setStrNumber(String strNumber) {
        this.strNumber = strNumber;
    }
}
