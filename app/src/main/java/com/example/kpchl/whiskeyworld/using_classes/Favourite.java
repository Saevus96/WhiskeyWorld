package com.example.kpchl.whiskeyworld.using_classes;

public class Favourite {

    private String name;
    private String icon;
    private Boolean isFavourite;

    public Favourite(String name, String icon, Boolean isFavourite) {
        this.name = name;
        this.icon = icon;
        this.isFavourite = isFavourite;
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

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}
