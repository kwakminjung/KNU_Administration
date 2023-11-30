package com.example.knu_administration_android.dto;

import com.google.gson.annotations.SerializedName;

public class HasteDto {
    @SerializedName("memberId")
    private Long memberId;

    @SerializedName("boardId")
    private Long boardId;

    public Long getMemberId(){return memberId;}
    public void setMemberId(Long memberId){this.memberId=memberId;}

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
}