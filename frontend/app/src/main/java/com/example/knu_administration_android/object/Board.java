package com.example.knu_administration_android.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Board implements Parcelable {
    String title, writerId, date, content;
    int state;
    // state = 1:접수 , 2:처리중 , 3:처리완료
    boolean isModified;

    // 글 작성 시 생성자
    public Board(String title, String writerId, String content, int state) {
        this.title = title;
        this.writerId = writerId;
        this.content = content;
        this.state = state;
    }

    // 글 불러올 시 생성자 (글 상세 볼 때, 글 수정 할 때)
    public Board(String title, String writerId, String date, String content, int state, boolean isModified) {
        this(title, writerId, content, state);
        this.date = date;
        this.isModified = isModified;
    }

    public String getTitle() {
        return title;
    }

    public String getWriterId() {
        return writerId;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public int getState() {
        return state;
    }

    public boolean getIsModified() {
        return isModified;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public void setDate() {
        Date d = new Date();
        SimpleDateFormat formatType = new SimpleDateFormat("yyyy-MM-dd | HH:mm");
        String str = formatType.format(d);
        this.date = str;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setModified(boolean isModified) {
        this.isModified = isModified;
    }


    protected Board(Parcel in) {
        title = in.readString();
        writerId = in.readString();
        date = in.readString();
        content = in.readString();
        state = in.readInt();
        isModified = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(writerId);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeInt(state);
        dest.writeByte((byte) (isModified ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };
}
