package com.example.knu_administration_android.request;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.knu_administration_android.dto.BoardDto;

public class BoardViewModel extends ViewModel {
    private final MutableLiveData<BoardDto> BoardInfo = new MutableLiveData<>();

    public void setSpecificBoardInfo(BoardDto boardInfo) {
        BoardInfo.setValue(boardInfo);
    }

    public MutableLiveData<BoardDto> getSpecificBoardInfo() {
        return BoardInfo;
    }
}