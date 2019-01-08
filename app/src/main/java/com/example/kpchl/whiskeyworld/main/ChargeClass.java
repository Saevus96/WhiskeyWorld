package com.example.kpchl.whiskeyworld.main;

public class ChargeClass {

    private double amount;
    private String cardId;
    private String currency;
    private String description;

    public ChargeClass(double amount, String cardId, String currency, String description) {
        this.amount = amount;
        this.cardId = cardId;
        this.currency = currency;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
