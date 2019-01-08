package com.example.kpchl.whiskeyworld.using_classes;

public class Comment {

    private String comment;
    private String email;
    private String userUrl;
    private String provider;

    public Comment(String comment, String email, String userUrl,String provider) {
        this.comment = comment;
        this.email = email;
        this.userUrl = userUrl;
        this.provider = provider;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
