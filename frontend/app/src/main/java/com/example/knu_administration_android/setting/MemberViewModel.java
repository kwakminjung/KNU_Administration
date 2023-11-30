package com.example.knu_administration_android.setting;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.knu_administration_android.dto.MemberDto;

public class MemberViewModel extends ViewModel {
    private final MutableLiveData<MemberDto> memberDto = new MutableLiveData<>();
    public void setMemberDto(MemberDto memberInfo) {memberDto.setValue(memberInfo);}
    public MutableLiveData<MemberDto> getMemberDto(){return memberDto;}
}
