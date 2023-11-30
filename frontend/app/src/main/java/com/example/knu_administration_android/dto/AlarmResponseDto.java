package com.example.knu_administration_android.dto;

import com.google.gson.annotations.SerializedName;

public class AlarmResponseDto {
    @SerializedName("boardId")
    private Long boardId;

    @SerializedName("noticeTitle")
    private String noticeTitle;

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }
}
