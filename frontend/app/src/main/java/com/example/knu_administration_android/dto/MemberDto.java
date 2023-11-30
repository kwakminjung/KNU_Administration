package com.example.knu_administration_android.dto;

import com.google.gson.annotations.SerializedName;

public class MemberDto {
    @SerializedName("memberId")
    private Long memberId;

    @SerializedName("accountId")
    private String accountId;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("boardNum")
    private int boardNum;


    public MemberDto(Long memberId, String accountId, String password, String name, int boardNum) {
        this.memberId=memberId;
        this.accountId = accountId;
        this.password = password;
        this.name = name;
        this.boardNum=boardNum;
    }
    public MemberDto(){}
    public void setMemberId(Long memberId){this.memberId=memberId;}

    public Long getMemberId() {
        return memberId;
    }

    public String getAccountId(){
        return accountId;
    }
    public void setAccountId(String accountId){
        this.accountId=accountId;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public int getBoardNum(){return boardNum;}
    public void setBoardNum(int boardNum){this.boardNum=boardNum;}





}