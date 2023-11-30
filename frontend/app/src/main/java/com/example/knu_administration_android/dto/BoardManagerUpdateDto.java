package com.example.knu_administration_android.dto;

import com.google.gson.annotations.SerializedName;

public class BoardManagerUpdateDto {
    @SerializedName("state")
    private int state;

    @SerializedName("category")
    private String category;


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}