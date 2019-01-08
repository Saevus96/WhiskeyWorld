package com.example.kpchl.whiskeyworld.using_classes;

public class SingleCreditCardInfo {
    private String last4;
    private String brand;
    private int exp_month;
    private int exp_year;
    private String key;
    private String cardId;

    public SingleCreditCardInfo(String last4, String brand, int exp_month, int exp_year, String key, String cardId) {
        this.last4 = last4;
        this.brand = brand;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.key = key;
        this.cardId = cardId;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getExp_month() {
        return exp_month;
    }

    public void setExp_month(int exp_month) {
        this.exp_month = exp_month;
    }

    public int getExp_year() {
        return exp_year;
    }

    public void setExp_year(int exp_year) {
        this.exp_year = exp_year;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
