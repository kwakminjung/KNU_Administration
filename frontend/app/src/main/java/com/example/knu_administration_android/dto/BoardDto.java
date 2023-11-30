package com.example.knu_administration_android.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BoardDto implements Serializable {
    @SerializedName("boardId")
    private Long boardId;

    @SerializedName("title")
    private String title;

    @SerializedName("writerId")
    private String writerId;

    @SerializedName("date")
    private String date;

    @SerializedName("content")
    private String content;

    @SerializedName("state")
    private int state;

    @SerializedName("isModified")
    private String isModified;

    @SerializedName("view")
    private int view;

    @SerializedName("hasteNum")
    private int hasteNum;

    @SerializedName("category")
    private String category;


    public Long getBoardId() {
        return boardId;
    }
    public void setBoardId(Long boardId){
        this.boardId=boardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriterId() {
        return writerId;
    }
    public void setWriterId(String writerId){
        this.writerId=writerId;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date){this.date=date;}
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsModified(){return isModified;}
    public void setIsModified(String isModified){this.isModified=isModified;}

    public int getHasteNum(){return hasteNum;}
    public void setHasteNum(int hasteNum){this.hasteNum=hasteNum;}
    public String getCategory(){return category;}
    public void setCategory(String category){this.category=category;}
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getView(){return view;}
    public void setView(int view){this.view = view;}
}