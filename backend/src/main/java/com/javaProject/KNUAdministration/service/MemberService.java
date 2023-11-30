package com.javaProject.KNUAdministration.service;

import com.javaProject.KNUAdministration.data.dto.MemberDto;

public interface MemberService {
    MemberDto insertMember(MemberDto dto);
//    Member selectMember(Long number);
//    List<Member> allSelectMember();
    MemberDto updateMember(MemberDto memberDto);
    MemberDto deleteMember(String AccountId);
    String userLogin(String accountId);
    Boolean isUserExists(String accountId);
    int viewBoardNum(String accountId);
    MemberDto showMember(String accountId);

}
