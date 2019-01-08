package com.example.kpchl.whiskeyworld.using_classes;

public class AddToCart {


    private int numberOfProduct;
    public String name;
    public String icon;
    public double price;

    public AddToCart(){

    }
    public AddToCart(int numberOfProduct, String name, String icon, double price) {
        this.numberOfProduct = numberOfProduct;
        this.name = name;
        this.icon = icon;
        this.price = price;
    }

    public int getNumberOfProduct() {
        return numberOfProduct;
    }

    public void setNumberOfProduct(int numberOfProduct) {
        this.numberOfProduct = numberOfProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
