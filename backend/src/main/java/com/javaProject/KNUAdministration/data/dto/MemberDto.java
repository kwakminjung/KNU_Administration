package com.javaProject.KNUAdministration.data.dto;

import com.javaProject.KNUAdministration.data.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long memberId;
    private String accountId;
    private String password;
    private String name;
    private int boardNum;

    public static MemberDto createMemberDto(Member member) {//엔티티를 dto로
        return new MemberDto(
                member.getMemberId(),
                member.getAccountId(),
                member.getPassword(),
                member.getName(),
                member.getBoardNum()
        );
    }

    public static Member resisterMemberEntity(MemberDto dto) {
        return new Member(
                null,
                dto.getAccountId(),
                dto.getPassword(),
                dto.getName(),
                0,
                null
        );
    }



}
