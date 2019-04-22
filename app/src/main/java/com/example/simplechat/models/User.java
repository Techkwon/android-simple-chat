package com.example.simplechat.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("result")
    private String result;

    @SerializedName("userId")
    private String userId;

    @SerializedName("userName")
    private String userName;


    public String getResult() {
        return result;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
